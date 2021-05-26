package com.beyond.event.driven.subscribe;

import org.springframework.amqp.core.Message;

public interface InboxStore {

    void save(Message message);

    boolean isConsumed(String key);

    void setConsumed(String key, String messageId);

}
