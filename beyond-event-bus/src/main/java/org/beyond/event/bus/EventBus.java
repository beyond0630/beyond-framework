package org.beyond.event.bus;

public interface EventBus {

    void register(SubscribeObject subscriber);

    void unregister(SubscribeObject subscriber);

    void post(Event event);

    void post(Event event, String topic);

    void close();

    String getBusName();
}
