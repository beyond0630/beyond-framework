package org.beyond.event.bus.impl;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

import org.beyond.event.bus.SubscribeObject;
import org.beyond.event.bus.Subscriber;

public class DefaultSubscriber implements Subscriber {

    private final SubscribeObject subscribeObject;
    private final Method subscribeMethod;
    private final AtomicBoolean enable = new AtomicBoolean(true);

    public DefaultSubscriber(final SubscribeObject subscribeObject, final Method subscribeMethod) {
        this.subscribeObject = subscribeObject;
        this.subscribeMethod = subscribeMethod;
    }

    @Override
    public SubscribeObject getSubscribeObject() {
        return this.subscribeObject;
    }

    @Override
    public Method getSubscribeMethod() {
        return this.subscribeMethod;
    }

    @Override
    public void disable() {
        enable.set(false);
    }

    @Override
    public boolean isEnable() {
        return enable.get();
    }
}
