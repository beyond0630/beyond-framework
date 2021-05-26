package com.beyond.event.driven.subscribe;

import java.util.List;

public interface EventHandlerInstanceExporter {

    List<EventHandler<?>> getInstances();

}
