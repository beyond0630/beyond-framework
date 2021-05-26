package org.beyond.event.bus.impl;

import org.beyond.event.bus.*;

public class SyncEventBus implements EventBus {

    private final static String DEFAULT_BUS_NAME = "default";
    private final static String DEFAULT_TOPIC = "default-topic";

    private final String busName;
    private final Register register;
    private final Dispatcher dispatcher;

    public SyncEventBus() {
        this(DEFAULT_BUS_NAME);
    }

    public SyncEventBus(final String busName) {
        this(busName, new DefaultRegister(new DefaultSubscriberFactory()));
    }

    public SyncEventBus(final String busName, final Register register) {
        this(busName, register, null, null);
    }

    public SyncEventBus(final String busName,
                        final Register register,
                        final Executor executor,
                        final EventExceptionHandler eventExceptionHandler) {
        this.busName = busName;
        this.register = register;
        this.dispatcher = new DefaultDispatcher(executor, eventExceptionHandler);
    }

    @Override
    public void register(final SubscribeObject subscriber) {
        this.register.bind(subscriber);
    }

    @Override
    public void unregister(final SubscribeObject subscriber) {
        this.register.unbind(subscriber);
    }

    @Override
    public void post(final Event event) {
        this.post(event, DEFAULT_TOPIC);
    }

    @Override
    public void post(final Event event, final String topic) {
        this.dispatcher.dispatch(this, this.register, event, topic);
    }

    @Override
    public void close() {
        this.dispatcher.close();
    }

    @Override
    public String getBusName() {
        return this.busName;
    }
}
