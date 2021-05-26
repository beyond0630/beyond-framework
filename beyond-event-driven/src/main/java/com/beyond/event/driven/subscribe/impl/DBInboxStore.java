package com.beyond.event.driven.subscribe.impl;

import java.nio.charset.StandardCharsets;

import com.beyond.event.driven.conts.MessageHeaders;
import com.beyond.event.driven.enums.BooleanEnum;
import com.beyond.event.driven.model.entities.InboxMessage;
import com.beyond.event.driven.service.MessageService;
import com.beyond.event.driven.subscribe.InboxStore;
import com.beyond.event.driven.utils.JsonUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

public class DBInboxStore implements InboxStore {

    private final MessageService messageService;

    public DBInboxStore(final MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void save(final Message message) {

        final MessageProperties properties = message.getMessageProperties();
        final String messageId = properties.getMessageId();

        if (this.messageService.isInboxMessageIdExists(messageId)) {
            return;
        }

        InboxMessage record = new InboxMessage();
        record.setMessageId(messageId);
        record.setIsBinary(BooleanEnum.FALSE.getValue());
        record.setExchange(properties.getHeader(MessageHeaders.X_ORIGIN_EXCHANGE));
        record.setRoutingKey(properties.getHeader(MessageHeaders.X_ORIGIN_ROUTING_KEY));
        record.setContentType(properties.getContentType());
        record.setMessageType(properties.getType());
        record.setProperties(JsonUtils.serialize(properties));
        record.setHeader(JsonUtils.serialize(properties.getHeaders()));
        record.setBody(new String(message.getBody(), StandardCharsets.UTF_8));
        this.messageService.addInboxMessage(record);

    }

    @Override
    public boolean isConsumed(final String key) {
        return this.messageService.isInboxMessageHandled(key);
    }

    @Override
    public void setConsumed(final String key, final String messageId) {
        this.messageService.setInboxMessageHandled(key, messageId);
    }

}
