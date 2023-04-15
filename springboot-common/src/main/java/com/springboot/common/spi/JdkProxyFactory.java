package com.springboot.common.spi;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkProxyFactory extends ProxyFactory {
    public JdkProxyFactory() {
        log.info("加载JdkProxyFactory");
    }

    @Override
    public <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new JdkDynamicProxy(target));
    }
}
