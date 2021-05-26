package org.beyond.event.bus.impl;

import java.lang.reflect.Method;
import java.util.Optional;

import org.beyond.event.bus.Event;
import org.beyond.event.bus.EventContext;
import org.beyond.event.bus.SubscribeObject;
import org.beyond.event.bus.Subscriber;

public class DefaultEventContext implements EventContext {

    private final String eventBusName;
    private final Subscriber subscriber;
    private final Event event;

    public DefaultEventContext(final String eventBusName, final Subscriber subscriber, final Event event) {
        this.eventBusName = eventBusName;
        this.subscriber = subscriber;
        this.event = event;
    }

    @Override
    public String getEventBusName() {
        return this.eventBusName;
    }

    @Override
    public SubscribeObject getSubscriber() {
        return Optional.ofNullable(subscriber).map(Subscriber::getSubscribeObject).orElse(null);
    }

    @Override
    public Method getSubscribe() {
        return Optional.ofNullable(subscriber).map(Subscriber::getSubscribeMethod).orElse(null);
    }

    @Override
    public Event getEvent() {
        return this.event;
    }

    @Override
    public String toString() {
        return "DefaultEventContext{" +
                "eventBusName='" + eventBusName + '\'' +
                ", subscriber=" + subscriber +
                ", event=" + event +
                '}';
    }
}
