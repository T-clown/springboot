package com.springboot.common.aop;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.springboot.common.aop.annotation.LockKeyParam;
import com.springboot.common.aop.annotation.RedisLock;
import com.springboot.common.exception.ServiceException;
import com.springboot.utils.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * set命令要用set key value px milliseconds nx；
 * value要具有唯一性；
 * 释放锁时要验证value值，不能误解锁；
 */
@Aspect
@Component
@Slf4j
public class RedisLockAspect {

    private static final String KEY_PREFIX = "DISTRIBUTED_LOCK";

    private static final String KEY_SEPARATOR = "_";

    /**
     * 切入点
     */
    @Pointcut("@annotation(com.springboot.common.aop.annotation.RedisLock)")
    public void doLock() {

    }

    /**
     * 环绕操作
     *
     * @param point 切入点
     * @return 原方法返回值
     * @throws Throwable 异常信息
     */
    @Around("doLock()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        Object[] args = point.getArgs();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        if (StrUtil.isBlank(redisLock.key())) {
            throw new ServiceException("分布式锁键不能为空");
        }
        String lockKey = buildLockKey(redisLock, method, args);
        boolean lock=false;
        try {
             lock = RedisLockUtil.tryLock(lockKey, redisLock.timeUnit(), redisLock.timeout(), redisLock.expireTime());
            // 假设上锁成功，以后拿到的都是 false
            if (lock) {
                return point.proceed();
            } else {
                throw new ServiceException("请勿重复提交");
            }
        } finally {
            if(lock){
                RedisLockUtil.unlock(lockKey);
            }
        }
    }

    /**
     * 构造分布式锁的键
     *
     * @param lock   注解
     * @param method 注解标记的方法
     * @param args   方法上的参数
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private String buildLockKey(RedisLock lock, Method method, Object[] args)
            throws NoSuchFieldException, IllegalAccessException {
        StringBuilder key = new StringBuilder( KEY_PREFIX + KEY_SEPARATOR+lock.key());

        // 迭代全部参数的注解，根据使用LockKeyParam的注解的参数所在的下标，来获取args中对应下标的参数值拼接到前半部分key上
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            // 循环该参数全部注解
            for (Annotation annotation : parameterAnnotations[i]) {
                // 注解不是 @LockKeyParam
                if (!(annotation instanceof LockKeyParam)) {
                    continue;
                }
                // 获取所有fields
                String[] fields = ((LockKeyParam)annotation).fields();
                if (ArrayUtil.isEmpty(fields)) {
                    // 普通数据类型直接拼接
                    if (ObjectUtil.isNull(args[i])) {
                        throw new RuntimeException("动态参数不能为null");
                    }
                    key.append(KEY_SEPARATOR).append(args[i]);
                } else {
                    // @LockKeyParam的fields值不为null，所以当前参数应该是对象类型
                    for (String field : fields) {
                        Class<?> clazz = args[i].getClass();
                        Field declaredField = clazz.getDeclaredField(field);
                        declaredField.setAccessible(true);
                        Object value = declaredField.get(args[i]);
                        key.append(KEY_SEPARATOR).append(value);
                    }
                }
            }
        }
        return key.toString();
    }

}
