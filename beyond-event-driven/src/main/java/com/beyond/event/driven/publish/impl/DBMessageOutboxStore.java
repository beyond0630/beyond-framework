package com.beyond.event.driven.publish.impl;

import java.nio.charset.StandardCharsets;

import com.beyond.event.driven.conts.MessageHeaders;
import com.beyond.event.driven.enums.BooleanEnum;
import com.beyond.event.driven.model.entities.OutboxMessage;
import com.beyond.event.driven.publish.MessageOutboxStore;
import com.beyond.event.driven.service.MessageService;
import com.beyond.event.driven.utils.JsonUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.util.Assert;

public class DBMessageOutboxStore implements MessageOutboxStore {

    private final MessageService messageService;

    public DBMessageOutboxStore(final MessageService messageService) {
        this.messageService = messageService;
    }


    @Override
    public void save(final Message message) {
        Assert.notNull(message, "message can not be null");
        final MessageProperties properties = message.getMessageProperties();
        final OutboxMessage record = new OutboxMessage();
        record.setMessageId(properties.getMessageId());
        record.setIsBinary(BooleanEnum.FALSE.getValue());
        record.setExchange(properties.getHeader(MessageHeaders.X_ORIGIN_EXCHANGE));
        record.setRoutingKey(properties.getHeader(MessageHeaders.X_ORIGIN_ROUTING_KEY));
        record.setContentType(properties.getContentType());
        record.setMessageType(properties.getType());
        record.setProperties(JsonUtils.serialize(properties));
        record.setHeader(JsonUtils.serialize(properties.getHeaders()));
        record.setBody(new String(message.getBody(), StandardCharsets.UTF_8));
        this.messageService.addOutboxMessage(record);
    }

    @Override
    public void deleteConfirmedMessage(final String id) {
        this.messageService.deleteOutboxUnconfirmed(id);
    }

    @Override
    public void updateOutboxRetried(final long id, final int interval) {

    }
}
