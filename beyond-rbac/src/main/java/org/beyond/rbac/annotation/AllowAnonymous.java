package org.beyond.rbac.annotation;

import java.lang.annotation.*;

import org.springframework.stereotype.Component;

/**
 * 标记一个请求或多个请求允许匿名访问
 *
 * @author Beyond
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface AllowAnonymous {
}
