package com.springboot.common.extension;

import com.springboot.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 使用场景：用户可以扩展这个类，来为要实例化的bean作一个代理，
 * 比如为该对象的所有的方法作一个拦截，在调用前后输出一行log，模仿ProxyFactoryBean的功能。
 * FactoryBean的作用是将复杂的Bean创建逻辑封装到一个单独的Bean中，以便于容器管理。当容器需要获取某个Bean时，
 * 会先从FactoryBean中获取该Bean，然后再通过该Bean创建出最终的Bean对象。
 * FactoryBean在Bean的创建过程中提供了更多的控制权，可以自定义Bean的创建过程，从而实现更加灵活的Bean创建和配置
 * 参照
 *
 * @see SqlSessionFactoryBean
 */
@Slf4j
@Component
public class TestFactoryBean implements FactoryBean<User>, InitializingBean {
    private User user;

    @Override
    public User getObject() throws Exception {
        log.error("[FactoryBean] getObject");
        return user;
    }

    @Override
    public Class<?> getObjectType() {
        return User.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        //创建bean
        this.user = createUser();
    }

    private User createUser() {
        return new User();
    }
}
