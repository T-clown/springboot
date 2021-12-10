package com.springboot.extend;

import com.springboot.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 使用场景：用户可以扩展这个类，来为要实例化的bean作一个代理，
 * 比如为该对象的所有的方法作一个拦截，在调用前后输出一行log，模仿ProxyFactoryBean的功能。
 */
@Slf4j
@Component
public class TestFactoryBean implements FactoryBean<User> {

    @Override
    public User getObject() throws Exception {
        log.info("[FactoryBean] getObject");
        return new User();
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
