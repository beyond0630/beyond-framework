package com.beyond.event.driven.subscribe;

import com.beyond.event.driven.common.Event;

public interface EventHandler<T extends Event> {

    void handle(T event);

}
