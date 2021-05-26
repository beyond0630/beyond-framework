package com.beyond.event.driven.publish.impl;

import com.beyond.event.driven.common.*;
import com.beyond.event.driven.publish.EventPublisher;
import com.beyond.event.driven.publish.MessageIdFactory;
import com.beyond.event.driven.publish.MessageOutboxStore;
import com.beyond.event.driven.publish.MessageSender;
import com.beyond.event.driven.utils.NameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static com.beyond.event.driven.conts.MessageHeaders.X_APP_NAME;
import static com.beyond.event.driven.conts.MessageHeaders.X_JAVA_CLASS;
import static com.beyond.event.driven.conts.MessageHeaders.X_ORIGIN_EXCHANGE;
import static com.beyond.event.driven.conts.MessageHeaders.X_ORIGIN_ROUTING_KEY;

public class TransactionalRabbitEventPublisher implements EventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionalRabbitEventPublisher.class);

    private final MessageSender messageSender;
    private final EventSerializer eventSerializer;
    private final MessageIdFactory messageIdFactory;
    private final MessageOutboxStore messageOutboxStore;
    private final EventMetadataResolver eventMetadataResolver;
    private final EventPublisherOptions eventPublisherOptions;

    public TransactionalRabbitEventPublisher(final MessageSender messageSender,
                                             final EventSerializer eventSerializer,
                                             final MessageIdFactory messageIdFactory,
                                             final MessageOutboxStore messageOutboxStore,
                                             final EventMetadataResolver eventMetadataResolver,
                                             final EventPublisherOptions eventPublisherOptions) {
        this.messageSender = messageSender;
        this.eventSerializer = eventSerializer;
        this.messageIdFactory = messageIdFactory;
        this.messageOutboxStore = messageOutboxStore;
        this.eventMetadataResolver = eventMetadataResolver;
        this.eventPublisherOptions = eventPublisherOptions;
    }

    @Override
    public void publish(final Event event) {
        this.publish(event, null);
    }

    @Override
    public void publish(final Event event, final String tag) {
        final EventMetadata metadata = this.eventMetadataResolver.get(event.getClass());
        final String messageId = this.messageIdFactory.next();
        final byte[] body = eventSerializer.serialize(event);
        final String key = NameUtils.combineSuffix(metadata.getTopic(), tag);

        final MessageProperties properties = new MessageProperties();
        properties.setMessageId(messageId);
        properties.setType(metadata.getType());
        properties.setContentType(this.eventSerializer.getContentType());
        properties.setHeader(X_APP_NAME, this.eventPublisherOptions.getAppName());
        properties.setHeader(X_JAVA_CLASS, metadata.getClassName());
        properties.setHeader(X_ORIGIN_EXCHANGE, eventPublisherOptions.getDefaultExchange());
        properties.setHeader(X_ORIGIN_ROUTING_KEY, key);

        final Message message = new Message(body, properties);
        this.messageOutboxStore.save(message);

        this.send(this.eventPublisherOptions.getDefaultExchange(), key, message);
    }

    private void send(final String exchange, final String routingKey, final Message message) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

                @Override
                public void afterCommit() {
                    TransactionalRabbitEventPublisher
                            .this.messageSender.send(exchange, routingKey, message);
                }
            });
        } else {
            LOGGER.warn("Message publishing with out transaction, exchange:{}, routingKey:{}",
                    exchange, routingKey);
            this.messageSender.send(exchange, routingKey, message);
        }
    }
}
