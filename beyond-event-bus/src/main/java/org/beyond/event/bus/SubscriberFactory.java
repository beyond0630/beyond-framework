package org.beyond.event.bus;

import java.lang.reflect.Method;

public interface SubscriberFactory {

    Subscriber createSubscriber(final SubscribeObject subscriber, final Method method);
}
