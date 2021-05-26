package org.beyond.longpolling.common;

import org.springframework.web.context.request.async.DeferredResult;

public interface PollableMessageBroker<R> {

    DeferredResult<R> subscribe(String channel);

    void publish(String channel, Message message);

    void shutdown();

}
