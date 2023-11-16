package com.springboot.common.aop.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author ikun
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RedisCache {
    /**
     * cache data class
     *
     * @return
     */
    Class<?> targetClass();

    /**
     * cache key
     *
     * @return
     */
    String key();

    /**
     * is list
     *
     * @return
     */
    boolean isList() default false;

    /**
     * expire time
     *
     * @return
     */
    long expireTime() default 60;

    /**
     * expire time unit
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
