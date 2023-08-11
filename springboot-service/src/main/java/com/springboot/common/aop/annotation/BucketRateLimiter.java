package com.springboot.common.aop.annotation;

import com.springboot.common.aop.RateLimiterAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 漏桶限流
 * @Author
 * @see com.springboot.common.aop.RateLimiterAspect
 * {@link RateLimiterAspect#pointcut(ProceedingJoinPoint)} ()}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BucketRateLimiter {
    long DEFAULT_REQUEST = 10;

    /**
     * max 最大请求数
     */
    @AliasFor("max") long value() default DEFAULT_REQUEST;

    /**
     * max 最大请求数
     */
    @AliasFor("value") long max() default DEFAULT_REQUEST;

    /**
     * 限流key
     */
    String key() default "";

    /**
     * 超时时长，默认1分钟
     */
    long timeout() default 1;

    /**
     * 超时时间单位，默认 分钟
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
