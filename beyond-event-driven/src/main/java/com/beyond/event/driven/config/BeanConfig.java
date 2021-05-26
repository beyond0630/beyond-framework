package com.beyond.event.driven.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.beyond.event.driven.common.EventMetadataResolver;
import com.beyond.event.driven.common.EventSerializer;
import com.beyond.event.driven.common.IdFactory;
import com.beyond.event.driven.common.impl.DefaultEventMetadataResolver;
import com.beyond.event.driven.common.impl.JsonEventSerializer;
import com.beyond.event.driven.common.impl.SnowflakeIdFactory;
import com.beyond.event.driven.option.EventPublisherOptions;
import com.beyond.event.driven.option.EventRetryOptions;
import com.beyond.event.driven.option.EventSubscriptionOptions;
import com.beyond.event.driven.publish.EventPublisher;
import com.beyond.event.driven.publish.MessageIdFactory;
import com.beyond.event.driven.publish.MessageSender;
import com.beyond.event.driven.publish.OutboxStore;
import com.beyond.event.driven.publish.impl.DBOutboxStore;
import com.beyond.event.driven.publish.impl.RabbitMessageSender;
import com.beyond.event.driven.publish.impl.TransactionalRabbitEventPublisher;
import com.beyond.event.driven.publish.impl.UUIDMessageIdFactory;
import com.beyond.event.driven.service.MessageService;
import com.beyond.event.driven.subscribe.*;
import com.beyond.event.driven.subscribe.impl.*;
import com.beyond.event.driven.utils.NameUtils;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BeanConfig {

    private static final String APP_NAME = "beyond-event-driven";

    private static final String EXCHANGE = "beyond-event";

    private static final String EXCHANGE_DEAD_LETTER = "beyond-event-dead-letter";
    private static final String EXCHANGE_RETRY = "beyond-event-retry";
    private static final String QUEUE_DEAD_LETTER = "beyond-event-dead-letter";
    private static final int DEAD_LETTER_QUEUE_TTL = 120000;
    private static final String NAMESPACE = "beyond.eventdriven";

    @Bean
    public IdFactory idFactory() {
        return new SnowflakeIdFactory(0, 0);
    }

    @Bean
    public MessageIdFactory messageIdFactory() {
        return new UUIDMessageIdFactory();
    }

    @Bean
    public OutboxStore messageOutboxStore(MessageService messageService) {
        return new DBOutboxStore(messageService);
    }

    @Bean
    public EventSerializer eventSerializer() {
        return new JsonEventSerializer();
    }

    @Bean
    public EventMetadataResolver eventMetadataResolver() {
        return new DefaultEventMetadataResolver(() -> NAMESPACE);
    }

    @Bean
    public EventPublisherOptions eventPublisherOptions() {
        return new EventPublisherOptions() {

            @Override
            public String getAppName() {
                return APP_NAME;
            }

            @Override
            public String getDefaultExchange() {
                return EXCHANGE;
            }
        };
    }

    @Bean
    public MessageSender messageSender(final OutboxStore outboxStore,
                                       final ConnectionFactory connectionFactory) {
        return new RabbitMessageSender(outboxStore, connectionFactory);
    }

    @Bean
    public EventPublisher eventPublisher(final MessageSender messageSender,
                                         final EventSerializer eventSerializer,
                                         final MessageIdFactory messageIdFactory,
                                         final OutboxStore outboxStore,
                                         final EventMetadataResolver eventMetadataResolver,
                                         final EventPublisherOptions eventPublisherOptions) {
        return new TransactionalRabbitEventPublisher(messageSender, eventSerializer, messageIdFactory,
            outboxStore, eventMetadataResolver, eventPublisherOptions);
    }

    @Bean
    public InboxStore inboxStore(final MessageService messageService) {
        return new DBInboxStore(messageService);
    }

    @Bean
    public EventSubscriptionOptions eventSubscriptionOptions() {
        return new EventSubscriptionOptions() {

            @Override
            public String getDefaultExchange() {
                return EXCHANGE;
            }

            @Override
            public String getDeadLetterExchange() {
                return EXCHANGE_DEAD_LETTER;
            }

            @Override
            public String getRetryExchange() {
                return EXCHANGE_RETRY;
            }

            @Override
            public String getDefaultQueue() {
                return NameUtils.combineSuffix(NAMESPACE, "default");
            }

            @Override
            public String getDeadLetterQueue() {
                return QUEUE_DEAD_LETTER;
            }

            @Override
            public int getDeadLetterQueueTtl() {
                return DEAD_LETTER_QUEUE_TTL;
            }

            @Override
            public String getQueueNamespace() {
                return NAMESPACE;
            }
        };
    }

    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public EventHandlerInstanceExporter handlerInstanceExporter(final ListableBeanFactory beanFactory) {
        // 获取所有 EventHandler
        return () -> {
            final Map<String, EventHandler> beans = beanFactory.getBeansOfType(EventHandler.class);
            return (List<EventHandler<?>>) (Object) new ArrayList<>(beans.values());
        };
    }

    @Bean
    public EventSubscriptionRegistry eventSubscriptionRegistry(final EventMetadataResolver eventMetadataResolver,
                                                                      final EventSubscriptionOptions eventSubscriptionOptions,
                                                                      final EventHandlerInstanceExporter handlerInstanceExporter) {
        return new RabbitEventSubscriptionRegistry(eventMetadataResolver, eventSubscriptionOptions, handlerInstanceExporter);
    }

    @Bean
    public EventDispatcher eventDispatcher(final InboxStore inboxStore,
                                           final EventSerializer eventSerializer,
                                           final PlatformTransactionManager transactionManager,
                                           final EventSubscriptionRegistry subscriptionRegistry) {
        return new TransactionalRabbitEventDispatcher(inboxStore, eventSerializer, transactionManager, subscriptionRegistry);
    }

    @Bean
    public EventRetryOptions eventRetryOptions() {
        return new EventRetryOptions() {

            @Override
            public boolean isRetry() {
                return true;
            }

            @Override
            public int maxAttempts() {
                return 5;
            }

            @Override
            public long initialInterval() {
                return 2000L;
            }

            @Override
            public double multiplier() {
                return 2;
            }

            @Override
            public long maxInterval() {
                return 30000L;
            }
        };
    }

    @Bean(initMethod = "start")
    public EventSubscriberHost eventSubscriberHost(final EventDispatcher eventDispatcher,
                                                   final ConnectionFactory connectionFactory,
                                                   final EventRetryOptions eventRetryOptions,
                                                   final EventSubscriptionOptions subscriptionOptions,
                                                   final EventSubscriptionRegistry eventSubscriptionRegistry) {
        return new RabbitEventSubscriberHost(eventDispatcher, connectionFactory, eventRetryOptions,
            subscriptionOptions, eventSubscriptionRegistry);
    }
}
