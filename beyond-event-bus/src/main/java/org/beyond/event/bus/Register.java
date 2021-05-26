package org.beyond.event.bus;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface Register {

    void bind(final SubscribeObject subscriber);

    void unbind(final SubscribeObject subscriber);

    ConcurrentLinkedQueue<Subscriber> scanSubscribe(String topic);

}
