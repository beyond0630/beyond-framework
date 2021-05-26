package com.beyond.event.driven.subscribe;

import java.util.List;

public interface EventSubscription {

    String getId();

    String getName();

    String getQueue();

    String getEventType();

    List<String> getBindingKeys();

    Class<?> getEventClass();

    List<EventHandlerMethod> getMethods();

    void addMethod(EventHandlerMethod handlerMethod);

}
