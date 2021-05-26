package org.beyond.event.bus;

public interface EventExceptionHandler {

    void handler(Throwable e, EventContext eventContext);
}
