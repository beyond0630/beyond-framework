package com.beyond.event.driven.publish.impl;

import com.beyond.event.driven.publish.OutboxStore;
import org.springframework.amqp.core.Message;

public class NoopOutboxStore implements OutboxStore {

    @Override
    public void save(final Message message) {

    }

    @Override
    public void deleteConfirmedMessage(final String id) {
    }

    @Override
    public void updateOutboxRetried(final long id, final int interval) {

    }
}
