package com.beyond.event.driven.subscribe.impl;

import java.util.ArrayList;
import java.util.List;

import com.beyond.event.driven.subscribe.EventHandlerMethod;
import com.beyond.event.driven.subscribe.EventSubscription;

/**
 * @author Raven
 */
public class RabbitEventSubscription implements EventSubscription {

    private String id;
    private String name;
    private String queue;
    private String eventType;
    private List<String> bindingKeys = new ArrayList<>();
    private Class<?> eventClass;
    private List<EventHandlerMethod> methods = new ArrayList<>();

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String getQueue() {
        return this.queue;
    }

    public void setQueue(final String queue) {
        this.queue = queue;
    }

    @Override
    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    @Override
    public List<String> getBindingKeys() {
        return bindingKeys;
    }

    public void setBindingKeys(final List<String> bindingKeys) {
        this.bindingKeys = bindingKeys;
    }

    public void addBindingKey(final String bindingKey) {
        this.bindingKeys.add(bindingKey);
    }

    @Override
    public Class<?> getEventClass() {
        return this.eventClass;
    }

    public void setEventClass(final Class<?> eventClass) {
        this.eventClass = eventClass;
    }

    @Override
    public List<EventHandlerMethod> getMethods() {
        return this.methods;
    }

    public void setMethods(final List<EventHandlerMethod> methods) {
        this.methods = methods;
    }

    @Override
    public void addMethod(EventHandlerMethod method) {
        this.methods.add(method);
    }

}
