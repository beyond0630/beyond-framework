package com.beyond.event.driven.test.handler;

import com.beyond.event.driven.annotation.EventHandlerComponent;
import com.beyond.event.driven.subscribe.EventHandler;
import com.beyond.event.driven.test.event.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EventHandlerComponent
public class TestEventHandler implements EventHandler<TestEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestEventHandler.class);

    @Override
    public void handle(final TestEvent event) {
        LOGGER.debug("handler event {}", event);
    }

}
