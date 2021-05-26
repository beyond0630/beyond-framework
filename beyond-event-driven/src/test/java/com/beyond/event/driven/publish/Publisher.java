package com.beyond.event.driven.publish;

import com.beyond.event.driven.BeyondEventDrivenApplication;
import com.beyond.event.driven.common.Event;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BeyondEventDrivenApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Publisher {

    @Autowired
    private EventPublisher eventPublisher;

    @Test
    public void testPublishEvent() {

        final TestEvent testEvent = new TestEvent();
        testEvent.setContent("test content");
        eventPublisher.publish(testEvent);
    }

    private static final class TestEvent implements Event {

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(final String content) {
            this.content = content;
        }
    }
}
