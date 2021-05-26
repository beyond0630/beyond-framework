package com.beyond.event.driven.common;

public interface EventMetadata {

    String getClassName();

    String getNamespace();

    String getType();

    String getQualifiedType();

    String getTopic();

}
