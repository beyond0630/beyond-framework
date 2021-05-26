package com.beyond.event.driven.common;

public interface EventSerializer {

    String getContentType();

    byte[] serialize(Event event);

    <T extends Event> T deserialize(byte[] data, Class<T> clazz);

}