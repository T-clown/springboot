package com.springboot.common.aop.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁
 * @author clown
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * key
     */
    String key();

    /**
     * 轮询锁的时间
     * @return
     */
    int timeout() default 0;

    /**
     * key在redis里存在的时间，1000S
     * @return
     */
    int expireTime() default 30;

    /**
     * 时间格式，默认：毫秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}