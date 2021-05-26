package com.beyond.event.driven.common.impl;

import com.beyond.event.driven.common.EventMetadata;

public class DefaultEventMetadata implements EventMetadata {

    private Class<?> javaClass;
    private String className;
    private String namespace;
    private String type;
    private String qualifiedType;
    private String topic;


    public Class<?> getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(final Class<?> javaClass) {
        this.javaClass = javaClass;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(final String className) {
        this.className = className;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public String getQualifiedType() {
        return qualifiedType;
    }

    public void setQualifiedType(final String qualifiedType) {
        this.qualifiedType = qualifiedType;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    public void setTopic(final String topic) {
        this.topic = topic;
    }
}
