package com.beyond.event.driven.subscribe;

import java.lang.reflect.Method;

public interface EventHandlerMethod {

    String getEventType();

    Class<?> getEventClass();

    Class<?> getHandlerClass();

    Method getHandlerMethod();

    Object getHandlerInstance();

    void invoke(Object event) throws Exception;
}
