package com.beyond.event.driven.option;

public interface EventRetryOptions {

    /**
     * 是否重度
     */
    boolean isRetry();

    /**
     * 重试次数
     */
    int maxAttempts();

    /**
     * 初始重试间隔
     */
    long initialInterval();

    /**
     * 重试间隔乘积数
     */
    double multiplier();

    /**
     * 最大重试间隔
     */
    long maxInterval();
}
