package com.beyond.event.driven.subscribe.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.beyond.event.driven.option.EventRetryOptions;
import com.beyond.event.driven.option.EventSubscriptionOptions;
import com.beyond.event.driven.subscribe.EventDispatcher;
import com.beyond.event.driven.subscribe.EventSubscriberHost;
import com.beyond.event.driven.subscribe.EventSubscription;
import com.beyond.event.driven.subscribe.EventSubscriptionRegistry;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.classify.SubclassClassifier;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

public class RabbitEventSubscriberHost implements EventSubscriberHost, InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitEventSubscriberHost.class);

    private final AtomicBoolean inited = new AtomicBoolean();

    private static final String BINDING_KEY_ALL = "#";

    private final AmqpAdmin amqpAdmin;
    private final RetryOperations retry;
    private final EventDispatcher eventDispatcher;
    private final EventRetryOptions eventRetryOptions;
    private final ConnectionFactory connectionFactory;
    private final EventSubscriptionOptions subscriptionOptions;
    private final EventSubscriptionRegistry eventSubscriptionRegistry;
    private final List<SimpleMessageListenerContainer> containers = new ArrayList<>();

    public RabbitEventSubscriberHost(final EventDispatcher eventDispatcher,
                                     final ConnectionFactory connectionFactory,
                                     final EventRetryOptions eventRetryOptions,
                                     final EventSubscriptionOptions subscriptionOptions,
                                     final EventSubscriptionRegistry eventSubscriptionRegistry) {
        this.eventDispatcher = eventDispatcher;
        this.connectionFactory = connectionFactory;
        this.eventRetryOptions = eventRetryOptions;
        this.subscriptionOptions = subscriptionOptions;
        this.eventSubscriptionRegistry = eventSubscriptionRegistry;
        this.amqpAdmin = this.initAmqpAdmin();
        this.retry = this.initRetryOperations();
    }

    protected void onMessage(final Message message, final Channel channel) throws Exception {
        this.retry.execute((RetryCallback<Object, Exception>) context -> {
            this.eventDispatcher.dispatch(message);
            return null;
        });
    }

    private RabbitAdmin initAmqpAdmin() {
        final RabbitAdmin rabbitAdmin = new RabbitAdmin(this.connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        rabbitAdmin.setIgnoreDeclarationExceptions(false);
        return rabbitAdmin;
    }

    private RetryOperations initRetryOperations() {
        RetryPolicy retryPolicy;
        if (!this.eventRetryOptions.isRetry()) {
            retryPolicy = new NeverRetryPolicy();
        } else {
            final SimpleRetryPolicy defaultRetryPolicy = new SimpleRetryPolicy();
            defaultRetryPolicy.setMaxAttempts(this.eventRetryOptions.maxAttempts());

            final Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
            policyMap.put(NullPointerException.class, new NeverRetryPolicy());

            retryPolicy = new ExceptionClassifierRetryPolicy();
            ((ExceptionClassifierRetryPolicy) retryPolicy).setExceptionClassifier(new SubclassClassifier<>(policyMap, defaultRetryPolicy));
        }


        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(this.eventRetryOptions.initialInterval());
        backOffPolicy.setMultiplier(this.eventRetryOptions.multiplier());
        backOffPolicy.setMaxInterval(this.eventRetryOptions.maxInterval());

        final RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Override
    public void start() {
        if (!this.inited.get()) {
            throw new IllegalStateException("Initialization not completed");
        }
        containers.forEach(AbstractMessageListenerContainer::start);
        LOGGER.info("Started");
    }

    @Override
    public void destroy() throws Exception {
        this.containers.forEach(AbstractMessageListenerContainer::destroy);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化完成调用方法
        this.initialize();
    }

    private void initialize() {
        if (this.inited.get()) {
            return;
        }

        this.declareAmqpObjects();

        this.inited.set(true);
    }

    private SimpleMessageListenerContainer initContainer(final String... queueNames) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(this.connectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setDefaultRequeueRejected(false);
        container.setMessageListener((ChannelAwareMessageListener) this::onMessage);
        container.setQueueNames(queueNames);
        container.setAutoStartup(false);
        return container;
    }

    private void declareAmqpObjects() {

        // 获取事件订阅描述
        final List<EventSubscription> subscriptions = this.eventSubscriptionRegistry.getSubscriptions();

        // 创建消息监听器容器
        for (final EventSubscription subscription : subscriptions) {
            final SimpleMessageListenerContainer container = this.initContainer(subscription.getQueue());
            this.containers.add(container);
        }

        final Exchange exchange = this.declareExchange();
        final Exchange dlExchange = this.declareDeadLetterExchange();
        final Exchange retryExchange = this.declareRetryExchange();
        final Queue dlQueue = this.declareDeadLetterQueue();

        // 绑定：死信交换 -> 死信队列
        this.declareBinding(dlExchange, dlQueue, BINDING_KEY_ALL);

        for (final EventSubscription subscription : subscriptions) {
            final String retryBindingKey = subscription.getQueue();
            final Queue queue = this.declareQueue(subscription.getQueue(), retryBindingKey);

            for (final String bindingKey : subscription.getBindingKeys()) {
                // 绑定：发布交换 -> 订阅队列
                this.declareBinding(exchange, queue, bindingKey);
            }

            // 绑定：重试交换 -> 订阅队列
            this.declareBinding(retryExchange, queue, retryBindingKey);
        }
    }

    private void declareBinding(final Exchange exchange, final Queue queue, final String bindingKey) {
        final Binding binding = BindingBuilder.bind(queue)
            .to(exchange)
            .with(bindingKey)
            .noargs();
        this.amqpAdmin.declareBinding(binding);
        LOGGER.debug("Declare Binding: {}", binding);
    }

    private void deleteQueue(final String queueName) {
        this.amqpAdmin.deleteQueue(queueName, true, true);
        LOGGER.warn("Delete Queue: {}", queueName);
    }

    private Queue declareQueue(final String queueName, final String deadLetterRoutingKey) {
        final Queue queue = QueueBuilder.durable(queueName)
            .deadLetterExchange(this.subscriptionOptions.getDeadLetterExchange())
            .deadLetterRoutingKey(deadLetterRoutingKey)
            .build();
        this.amqpAdmin.declareQueue(queue);
        LOGGER.debug("Declare Queue: {}", queue);
        return queue;
    }

    private Queue declareDeadLetterQueue() {
        final Queue queue = QueueBuilder.durable(this.subscriptionOptions.getDeadLetterQueue())
            .deadLetterExchange(this.subscriptionOptions.getRetryExchange())
            .ttl(this.subscriptionOptions.getDeadLetterQueueTtl())
            .build();
        this.amqpAdmin.declareQueue(queue);
        LOGGER.debug("Declare Queue: {}", queue);
        return queue;
    }

    private Exchange declareExchange() {
        final Exchange exchange = ExchangeBuilder.topicExchange(this.subscriptionOptions.getDefaultExchange())
            .durable(true)
            .build();
        this.amqpAdmin.declareExchange(exchange);
        LOGGER.debug("Declare Exchange: {}", exchange);
        return exchange;
    }

    private Exchange declareDeadLetterExchange() {
        final Exchange exchange = ExchangeBuilder.topicExchange(this.subscriptionOptions.getDeadLetterExchange())
            .durable(true)
            .build();
        this.amqpAdmin.declareExchange(exchange);
        LOGGER.debug("Declare Exchange: {}", exchange);
        return exchange;
    }

    private Exchange declareRetryExchange() {
        final Exchange exchange = ExchangeBuilder.topicExchange(this.subscriptionOptions.getRetryExchange())
            .durable(true)
            .build();
        this.amqpAdmin.declareExchange(exchange);
        LOGGER.debug("Declare Exchange: {}", exchange);
        return exchange;
    }

}
