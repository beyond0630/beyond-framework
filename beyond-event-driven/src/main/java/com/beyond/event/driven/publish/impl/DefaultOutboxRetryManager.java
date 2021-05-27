package com.beyond.event.driven.publish.impl;

import java.util.List;

import com.beyond.event.driven.conts.MessageHeaders;
import com.beyond.event.driven.publish.MessageSender;
import com.beyond.event.driven.publish.OutboxRetryManager;
import com.beyond.event.driven.publish.OutboxStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

public class DefaultOutboxRetryManager implements OutboxRetryManager {

    private static final int RETRY_INTERVAL_SECONDS = 60;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOutboxRetryManager.class);

    private final OutboxStore outboxStore;
    private final MessageSender messageSender;

    public DefaultOutboxRetryManager(final OutboxStore outboxStore,
                                     final MessageSender messageSender) {
        this.outboxStore = outboxStore;
        this.messageSender = messageSender;
    }

    @Override
    public void performRetry() {
        while (true) {
            final int count = this.listAndSend();
            if (count < 1) {
                break;
            }
        }
    }

    protected int listAndSend() {
        final List<Message> messages = this.outboxStore.listUnconfirmed(100);
        if (messages == null || messages.isEmpty()) {
            return 0;
        }

        final int size = messages.size();
        LOGGER.info("Unconfirmed messages found: {}", size);

        for (final Message message : messages) {
            this.send(message);
        }
        return size;
    }

    protected void send(final Message message) {
        final MessageProperties props = message.getMessageProperties();
        final String exchange = props.getHeader(MessageHeaders.X_ORIGIN_EXCHANGE);
        final String routingKey = props.getHeader(MessageHeaders.X_ORIGIN_ROUTING_KEY);
        this.outboxStore.updateOutboxRetried(props.getMessageId(), RETRY_INTERVAL_SECONDS);
        this.messageSender.send(exchange, routingKey, message);
    }

}
