package com.beyond.event.driven.service;

import com.beyond.event.driven.model.entities.InboxMessage;
import com.beyond.event.driven.model.entities.OutboxMessage;

public interface MessageService {

    void addOutboxMessage(OutboxMessage outboxMessage);

    void deleteOutboxUnconfirmed(String id);

    void updateOutboxRetried(String id, int interval);

    void addInboxMessage(InboxMessage message);

    boolean isInboxMessageIdExists(String messageId);

    void setInboxMessageHandled(String key, String messageId);

    boolean isInboxMessageHandled(String key);
}
