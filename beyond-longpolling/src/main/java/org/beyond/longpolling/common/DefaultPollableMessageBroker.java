package org.beyond.longpolling.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.web.context.request.async.DeferredResult;

public class DefaultPollableMessageBroker<R> implements PollableMessageBroker<R> {

    private final ReentrantLock lock = new ReentrantLock(true);
    private final ConcurrentMap<String, MessageChannel<R>> channels = new ConcurrentHashMap<>();
    private final MessageChannelFactory<R> channelFactory;

    public DefaultPollableMessageBroker(final MessageChannelFactory<R> channelFactory) {
        this.channelFactory = channelFactory;
    }

    @Override
    public DeferredResult<R> subscribe(final String channelId) {
        return this.getMessageChannel(channelId)
            .subscribe();
    }

    @Override
    public void publish(final String channel, final Message message) {
        this.getMessageChannel(channel)
            .publish(message);
    }

    @Override
    public void shutdown() {
        this.lock.lock();
        try {
            this.channels.forEach((k, v) -> v.close());
        } finally {
            this.lock.unlock();
        }
    }

    private MessageChannel<R> getMessageChannel(final String channelId) {
        return this.channels.computeIfAbsent(channelId, this.channelFactory::newChannel);
    }

}
