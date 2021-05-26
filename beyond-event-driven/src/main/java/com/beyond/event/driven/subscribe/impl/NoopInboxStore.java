package com.beyond.event.driven.subscribe.impl;

import com.beyond.event.driven.subscribe.InboxStore;
import org.springframework.amqp.core.Message;

public class NoopInboxStore implements InboxStore {

    @Override
    public void save(final Message message) {

    }

    @Override
    public boolean isConsumed(final String key) {
        return false;
    }

    @Override
    public void setConsumed(final String key, final String messageId) {

    }

}
