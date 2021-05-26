package org.beyond.event.bus.impl;

import org.beyond.event.bus.Event;
import org.beyond.event.bus.SubscribeObject;
import org.beyond.event.bus.annotation.Subscribe;

public class SimpleSubscribe implements SubscribeObject {

    @Subscribe
    public void test1(Event event) {
        System.out.println("test1 + event[" + event.toString() + "]");
    }

    @Subscribe(topic = "haha")
    public void test2(Event event) {
        System.out.println("test2 + event[" + event.toString() + "]");
    }
}
