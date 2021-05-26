package org.beyond.longpolling.common;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import org.beyond.longpolling.common.redis.RedisMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.async.DeferredResult;

public abstract class BaseMessageChannel<R> implements MessageChannel<R> {

    private static final long SUBSCRIBE_TIMEOUT = 1000L * 60;

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMessageChannel.class);

    private final Lock lock = new ReentrantLock(true);

    private final String id;

    private final ResultConverter<R> converter;

    private final MessageQueue messageQueue;

    private DeferredResult<R> result;

    public BaseMessageChannel(final String id,
                              final ResultConverter<R> converter,
                              final RedisTemplate<String, Object> redisTemplate) {
        this.id = id;
        this.converter = converter;
        this.messageQueue = new RedisMessageQueue(id, redisTemplate);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public DeferredResult<R> subscribe() {
        return withLock(this::replaceDeferredResult);
    }

    @Override
    public void publish(final Message message) {
        // 消息进入队列
        this.enqueueMessage(message);
        // 判断是否已经订阅
        if (this.isResultNotAvailable()) {
            return;
        }
        // 推送消息到客户端
        this.retrieveMessages();
    }

    @Override
    public abstract void close();

    private void enqueueMessage(final Message message) {
        messageQueue.pushMessage(message);
    }

    private void clearDeferredResult() {
        if (this.isResultNotAvailable()) {
            return;
        }
        this.result.setResult(this.getEmptyResult());
    }

    private DeferredResult<R> replaceDeferredResult() {
        this.clearDeferredResult();
        this.result = this.newDeferredResult();
        this.retrieveMessages();
        return this.result;
    }


    private DeferredResult<R> newDeferredResult() {
        DeferredResult<R> result = new DeferredResult<>(SUBSCRIBE_TIMEOUT, this::getEmptyResult);
        result.onTimeout(this::onResultTimeout);
        result.onCompletion(this::onResultCompletion);
        result.onError(this::onResultError);
        return result;
    }

    private void retrieveMessages() {
        final List<Message> list = this.messageQueue.popAllMessages();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        this.result.setResult(this.converter.convert(list));
    }

    private boolean isResultNotAvailable() {
        return this.result == null || this.result.isSetOrExpired();
    }

    private void onResultTimeout() {
        LOGGER.debug("OnResultTimeout: {}", this.id);
    }

    private void onResultCompletion() {
        LOGGER.trace("OnResultCompletion: {}", this.id);
    }

    private void onResultError(final Throwable error) {
        LOGGER.error("OnResultError", error);
    }

    private R getEmptyResult() {
        return converter.convert(Collections.emptyList());
    }


    private <T> T withLock(Supplier<T> supplier) {
        this.lock.lock();
        try {
            return supplier.get();
        } finally {
            this.lock.unlock();
        }
    }
}
