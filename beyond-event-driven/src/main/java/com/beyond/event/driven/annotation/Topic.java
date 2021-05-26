package com.beyond.event.driven.annotation;

import java.lang.annotation.*;

/**
 * 标记一个类，指定它的自定义消息主题
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Topic {

    String value();

}