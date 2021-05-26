package org.beyond.event.bus;

public interface Dispatcher {

    void dispatch(EventBus syncEventBus, Register register, Event event, String topic);

    void close();
}
