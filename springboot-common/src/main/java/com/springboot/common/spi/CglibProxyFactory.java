package com.springboot.common.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;

/**
 * Cglib动态代理工厂
 */
@Slf4j
public class CglibProxyFactory extends ProxyFactory {

    public CglibProxyFactory() {
        log.info("加载CglibProxyFactory");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createProxy(T target) {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new CglibDynamicProxy(target));
        return (T) enhancer.create();
    }

}
