package com.beyond.event.driven.test.event;

import com.beyond.event.driven.common.Event;

public class TestEvent implements Event {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "TestEvent{" +
            "content='" + content + '\'' +
            '}';
    }

}
