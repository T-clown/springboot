package com.springboot.common.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * cglib动态代理
 */
@Slf4j
public class CglibDynamicProxy implements MethodInterceptor, Serializable {

    private final Object target;

    public CglibDynamicProxy(Object target) {
        this.target = target;
    }

    public Object getTarget() {
        return this.target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        log.info("cglib动态代理");
        return proxy.invoke(target, args);
    }
}
