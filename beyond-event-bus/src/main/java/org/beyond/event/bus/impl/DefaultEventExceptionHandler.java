package org.beyond.event.bus.impl;

import org.beyond.event.bus.EventContext;
import org.beyond.event.bus.EventExceptionHandler;

public class DefaultEventExceptionHandler implements EventExceptionHandler {

    @Override
    public void handler(final Throwable e, final EventContext eventContext) {
        System.err.println(e.getMessage() + eventContext.toString());
    }
}
