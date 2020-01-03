package com.springboot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * set命令要用set key value px milliseconds nx；
 * value要具有唯一性；
 * 释放锁时要验证value值，不能误解锁；
 */
@Aspect
@Component
@Slf4j
public class RedisLockAspect {

}
