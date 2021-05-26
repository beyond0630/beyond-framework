package org.beyond.event.bus.impl;

import java.lang.reflect.Method;

import org.beyond.event.bus.SubscribeObject;
import org.beyond.event.bus.Subscriber;
import org.beyond.event.bus.SubscriberFactory;

public class DefaultSubscriberFactory implements SubscriberFactory {

    @Override
    public Subscriber createSubscriber(final SubscribeObject subscriber, final Method method) {
        return new DefaultSubscriber(subscriber, method);
    }
}
