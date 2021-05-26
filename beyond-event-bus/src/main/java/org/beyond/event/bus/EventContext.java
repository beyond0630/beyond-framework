package org.beyond.event.bus;

import java.lang.reflect.Method;

public interface EventContext {

    String getEventBusName();

    SubscribeObject getSubscriber();

    Method getSubscribe();

    Event getEvent();

    String toString();
}
