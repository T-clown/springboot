package com.springboot.common.aop;

import com.alibaba.fastjson.JSON;
import com.springboot.common.aop.annotation.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RedisCacheAspect {

    @Around(value = "@annotation(redisCache)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCache redisCache) throws Throwable {
        String cacheKey = redisCache.key();

        String cacheData ="";

        if (StringUtils.isNotBlank(cacheData)) {
            if (redisCache.isList()) {
                return JSON.parseArray(cacheData, redisCache.targetClass());
            }
            return JSON.parseObject(cacheData, redisCache.targetClass());
        }

        Object result = joinPoint.proceed();

        return result;
    }
}
