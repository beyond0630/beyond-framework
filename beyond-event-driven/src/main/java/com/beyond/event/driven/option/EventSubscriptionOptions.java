package com.beyond.event.driven.option;

/**
 * 事件订阅选项
 */
public interface EventSubscriptionOptions {

    /**
     * 订阅的交换机
     */
    String getDefaultExchange();

    /**
     * 死信交换机
     */
    String getDeadLetterExchange();

    /**
     * 重试交换机
     */
    String getRetryExchange();

    /**
     * 死信队列
     */
    String getDeadLetterQueue();

    /**
     * 死信消息超时时间
     */
    int getDeadLetterQueueTtl();

    /**
     * 队列命名空间
     */
    String getQueueNamespace();

}
