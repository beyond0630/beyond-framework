package com.beyond.event.driven.subscribe;

import java.util.List;

public interface EventHandlerDescriptor {

    String getEventType();

    Class<?> getEventClass();

    List<EventHandlerMethod> getMethods();

}
