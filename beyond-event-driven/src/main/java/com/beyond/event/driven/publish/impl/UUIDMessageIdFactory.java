package com.beyond.event.driven.publish.impl;

import java.util.UUID;

import com.beyond.event.driven.publish.MessageIdFactory;

public class UUIDMessageIdFactory implements MessageIdFactory {

    @Override
    public String next() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
