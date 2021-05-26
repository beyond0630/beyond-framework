package com.beyond.event.driven.publish.impl;

import java.util.UUID;

import com.beyond.event.driven.publish.OutboxStore;
import com.beyond.event.driven.publish.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class RabbitMessageSender implements MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMessageSender.class);

    private final OutboxStore outboxStore;
    private final RabbitTemplate rabbitTemplate;

    public RabbitMessageSender(final OutboxStore outboxStore,
                               final ConnectionFactory connectionFactory) {
        this.outboxStore = outboxStore;
        this.rabbitTemplate = initRabbitTemplate(connectionFactory);
    }

    protected RabbitTemplate initRabbitTemplate(final ConnectionFactory connectionFactory) {
        if (!connectionFactory.isPublisherConfirms()) {
            throw new IllegalStateException("ConnectionFactory dose not support publisher confirms.");
        }

        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this::confirmHandler);
        rabbitTemplate.setReturnCallback(this::returnHandler);
        rabbitTemplate.afterPropertiesSet();
        return rabbitTemplate;
    }

    @Override
    public void send(final String exchange, final String routingKey, final Message message) {
        final MessageProperties properties = message.getMessageProperties();
        Assert.notNull(properties, "Message properties can not be null.");
        Assert.notNull(message.getBody(), "Message body can not be null.");

        String correlationId = UUID.randomUUID().toString().replace("-", "");
        final String messageId = properties.getMessageId();
        if (!StringUtils.isEmpty(messageId)) {
            correlationId = String.format("%s:%s", correlationId, messageId);
        }

        CorrelationData correlationData = new CorrelationData(correlationId);
        this.rabbitTemplate.send(exchange, routingKey, message, correlationData);
    }

    protected void confirmHandler(final @Nullable CorrelationData data,
                                  final boolean ack,
                                  final @Nullable String cause) {
        if (!ack) {
            LOGGER.error("Message not confirmed: {}, {}", data, cause);
            return;
        }
        if (data == null || data.getId() == null) {
            LOGGER.error("Message dose not have correlation data: {}", data);
            return;
        }

        final String id = data.getId();
        final int i = id.indexOf(':');
        if (i < 0) {
            LOGGER.warn("Unrecognized correlation id: {}", id);
            return;
        }
        final String messageId = id.substring(i + 1);
        LOGGER.debug("Confirmed message id: {}", messageId);
        this.outboxStore.deleteConfirmedMessage(messageId);
    }

    protected void returnHandler(final Message message, final int replyCode, final String replyText,
                                 final String exchange, final String routingKey) {
        LOGGER.error("Message returned: {}, {}, {}", replyCode, replyText, message);
        final MessageProperties properties = message.getMessageProperties();
        if (properties == null) {
            LOGGER.error("Message dose not have properties");
            return;
        }
        final String messageId = properties.getMessageId();
        if (StringUtils.isEmpty(messageId)) {
            LOGGER.error("MessageProperties dose not have message id");
            return;
        }
        LOGGER.debug("Message returned: {}", messageId);
    }
}
