package org.beyond.event.bus;

import org.beyond.event.bus.impl.SimpleSubscribe;
import org.beyond.event.bus.impl.SyncEventBus;

public class EventBusMain {

    public static void main(String[] args) {
        final SyncEventBus eventBus = new SyncEventBus();
        eventBus.register(new SimpleSubscribe());
        eventBus.post(new Event() {

            @Override
            public String toString() {
                return "event 1";
            }
        });
        System.out.println("-----------------");
        eventBus.post(new Event() {

            @Override
            public String toString() {
                return "event 2";
            }
        }, "haha");
    }
}
