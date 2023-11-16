package com.springboot.common.aop;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

/**
 * 基于接口实现AOP
 * 切点配置InterfaceAopConfig
 */
@Slf4j
public class Advice implements MethodInterceptor, MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {
    /**
     * 环绕通知
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("===========进入around环绕方法！===========");
        // 调用方法的参数
        Object[] args = invocation.getArguments();
        // 调用的方法
        Method method = invocation.getMethod();
        // 获取目标对象
        Object target = invocation.getThis();
        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        Object returnValue = invocation.proceed();
        log.info("===========结束进入around环绕方法！=========== ");
        log.info("输出：" + args[0] + ";" + method + ";" + target + ";" + returnValue );
        return returnValue;
    }

    @Override
    public void before(Method method, Object[] objects, Object target) throws Throwable {
        log.info("===========进入beforeAdvice()============ ");
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        log.info("==========进入afterReturning()=========== ");
    }

    public void afterThrowing(Method method, Object[] args, Object target, Throwable throwable) {
        log.info("==========进入afterThrowing()=========== ");
    }

}
