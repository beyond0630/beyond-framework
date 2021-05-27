package com.beyond.event.driven.subscribe.impl;

import com.beyond.event.driven.common.EventSerializer;
import com.beyond.event.driven.subscribe.*;
import com.beyond.event.driven.utils.ConsumeKeyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

public class TransactionalEventDispatcher implements EventDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionalEventDispatcher.class);

    private final InboxStore inboxStore;
    private final EventSerializer eventSerializer;
    private final PlatformTransactionManager transactionManager;
    private final EventSubscriptionRegistry subscriptionRegistry;

    public TransactionalEventDispatcher(final InboxStore inboxStore,
                                              final EventSerializer eventSerializer,
                                              final PlatformTransactionManager transactionManager,
                                              final EventSubscriptionRegistry subscriptionRegistry) {
        this.inboxStore = inboxStore;
        this.eventSerializer = eventSerializer;
        this.transactionManager = transactionManager;
        this.subscriptionRegistry = subscriptionRegistry;
    }

    @Override
    public void dispatch(final Message message) throws Exception {
        Assert.notNull(message, "Argument `message` could not be null.");

        final TransactionStatus tx = this.beginTransaction();
        boolean committed = false;

        try {
            this.dispatchWithTransaction(message);
            this.transactionManager.commit(tx);
            committed = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        } finally {
            if (!committed && !tx.isCompleted()) {
                this.transactionManager.rollback(tx);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void dispatchWithTransaction(final Message message) throws Exception {
        final MessageProperties props = message.getMessageProperties();
        final String queue = props.getConsumerQueue();
        final String messageId = props.getMessageId();
        // SHA-1 压缩用于判断是否重复消费消息
        final String consumeKey = ConsumeKeyUtils.compute(queue, messageId);
        final String subscriptionId = queue + ":" + props.getType();

        final EventSubscription subscription = this.subscriptionRegistry.getSubscription(subscriptionId);
        if (subscription == null) {
            throw new IllegalStateException("Could not find EventSubscription for " + subscriptionId);
        }

        if (this.inboxStore.isConsumed(consumeKey)) {
            LOGGER.error("Message already consumed, queue:{}, messageId:{}", queue, messageId);
            return;
        }
        this.inboxStore.save(message);

        final Class<?> eventClass = subscription.getEventClass();
        final Object event = eventSerializer.deserialize(message.getBody(), (Class) eventClass);

        for (final EventHandlerMethod method : subscription.getMethods()) {
            method.invoke(event);
        }

        this.inboxStore.setConsumed(consumeKey, messageId);
    }

    private TransactionStatus beginTransaction() {
        final DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
        txDef.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return this.transactionManager.getTransaction(txDef);
    }

}
