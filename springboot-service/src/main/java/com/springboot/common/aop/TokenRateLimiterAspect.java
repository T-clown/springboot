package com.springboot.common.aop;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


import com.google.common.util.concurrent.RateLimiter;
import com.springboot.common.aop.annotation.TokenRateLimiter;
import com.springboot.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 令牌桶限流切面
 */
@Slf4j
@Aspect
@Component
public class TokenRateLimiterAspect {
    private static final ConcurrentMap<String, RateLimiter> RATE_LIMITER_CACHE = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.springboot.common.aop.annotation.TokenRateLimiter)")
    public void pointcut() {}

    @Around("pointcut()")
    public Object pointcut(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        // 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
        TokenRateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, TokenRateLimiter.class);
        if (rateLimiter != null && rateLimiter.qps() > TokenRateLimiter.NOT_LIMITED) {
            double qps = rateLimiter.qps();
            if (RATE_LIMITER_CACHE.get(method.getName()) == null) {
                // 初始化 QPS
                RATE_LIMITER_CACHE.put(method.getName(), RateLimiter.create(qps));
                log.info("【{}】的QPS设置为: {}", method.getName(), RATE_LIMITER_CACHE.get(method.getName()).getRate());
            }
            // 尝试获取令牌
            if (RATE_LIMITER_CACHE.get(method.getName()) != null && !RATE_LIMITER_CACHE.get(method.getName())
                .tryAcquire(rateLimiter.timeout(), rateLimiter.timeUnit())) {
                throw new ServiceException("手速太快了，慢点儿吧~");
            }
        }
        return point.proceed();
    }
}
