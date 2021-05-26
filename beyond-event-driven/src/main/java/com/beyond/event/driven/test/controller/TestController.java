package com.beyond.event.driven.test.controller;

import com.beyond.event.driven.publish.EventPublisher;
import com.beyond.event.driven.test.event.TestEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final EventPublisher eventPublisher;

    public TestController(final EventPublisher eventPublisher) {this.eventPublisher = eventPublisher;}

    @GetMapping("/test")
    public void publishEvent() {

        final TestEvent testEvent = new TestEvent();
        testEvent.setContent("text content");
        eventPublisher.publish(testEvent);

    }
}
