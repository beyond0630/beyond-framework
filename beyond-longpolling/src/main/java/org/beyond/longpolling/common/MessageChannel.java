package org.beyond.longpolling.common;

import org.springframework.web.context.request.async.DeferredResult;

public interface MessageChannel<R> {

    String getId();

    DeferredResult<R> subscribe();

    void publish(final Message message);

    void close();
}
