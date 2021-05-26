package com.beyond.event.driven.subscribe.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.beyond.event.driven.subscribe.EventHandlerMethod;

public class RabbitEventHandlerMethod implements EventHandlerMethod {

    private String eventType;
    private Class<?> eventClass;
    private Class<?> handlerClass;
    private Method handlerMethod;
    private Object handlerInstance;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
    }

    public Class<?> getEventClass() {
        return eventClass;
    }

    public void setEventClass(final Class<?> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    public void setHandlerClass(final Class<?> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(final Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public Object getHandlerInstance() {
        return handlerInstance;
    }

    public void setHandlerInstance(final Object handlerInstance) {
        this.handlerInstance = handlerInstance;
    }

    @Override
    public void invoke(final Object event) throws Exception {
        try {
            this.handlerMethod.invoke(this.handlerInstance, event);
        } catch (InvocationTargetException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw e;
        }
    }

}
