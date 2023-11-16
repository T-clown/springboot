package com.springboot.common.spi.proxy;

import com.springboot.common.utils.ServiceLoaderUtil;

import java.io.Serializable;

/**
 * copy自hutool
 *
 */
public abstract class ProxyFactory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 创建代理
     *
     * @param <T>    代理对象类型
     * @param target 被代理对象
     * @return 代理对象
     * @since 5.3.1
     */
    public <T> T proxy(T target) {
        return createProxy(target);
    }

    /**
     * 创建代理
     *
     * @param <T>    代理对象类型
     * @param target 被代理对象
     * @return 代理对象
     */
    public abstract <T> T createProxy(T target);

    /**
     * 根据用户引入Cglib与否自动创建代理对象
     *
     * @param <T>    切面对象类型
     * @param target 被代理对象
     * @return 代理对象
     */
    public static <T> T createDefaultFactoryProxy(T target) {
        return create().proxy(target);
    }

    /**
     * 根据用户引入Cglib与否创建代理工厂
     *
     * @return 代理工厂
     */
    public static ProxyFactory create() {
        return ServiceLoaderUtil.loadFirstAvailable(ProxyFactory.class);
    }
}
