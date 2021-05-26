package com.beyond.event.driven.publish;

import com.beyond.event.driven.common.Event;

public interface EventPublisher {

    void publish(Event event);

    void publish(Event event, String tag);
}
