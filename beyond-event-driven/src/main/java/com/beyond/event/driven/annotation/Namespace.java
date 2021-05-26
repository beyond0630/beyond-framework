package com.beyond.event.driven.annotation;

import java.lang.annotation.*;

/**
 * 标记一个类，指定它的命名空间，不同命名空间下允许存在同样的对象名称
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Namespace {

    String value();
}
