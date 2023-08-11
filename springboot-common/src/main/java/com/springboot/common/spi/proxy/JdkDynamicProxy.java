package com.springboot.common.spi.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * jdk动态代理
 */
@Slf4j
public class JdkDynamicProxy implements InvocationHandler {

    private final Object target;

    public JdkDynamicProxy(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("jdk动态代理");
        return method.invoke(target, args);
    }

}
