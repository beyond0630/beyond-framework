package org.beyond.event.bus;

import java.lang.reflect.Method;

public interface Subscriber {

    SubscribeObject getSubscribeObject();

    Method getSubscribeMethod();

    void disable();

    boolean isEnable();

}
