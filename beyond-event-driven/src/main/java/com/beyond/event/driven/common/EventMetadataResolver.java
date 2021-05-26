package com.beyond.event.driven.common;

public interface EventMetadataResolver {

    EventMetadata get(Class<?> clazz);

}
