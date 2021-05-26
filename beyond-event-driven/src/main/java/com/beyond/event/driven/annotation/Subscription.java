package com.beyond.event.driven.annotation;

import java.lang.annotation.*;

/**
 * 标记一个 MessageHandler，指定它的订阅参数
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Subscription {

    /**
     * 订阅的唯一名称
     */
    String value();

    /**
     * 订阅的消息 Tag
     */
    String[] tags();

}
