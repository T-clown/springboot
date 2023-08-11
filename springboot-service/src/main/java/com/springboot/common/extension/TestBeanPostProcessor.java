package com.springboot.common.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TestBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifeCycle) {
            log.error("LifeCycle初始化之前调用[BeanPostProcessor.postProcessBeforeInitialization] bean:{},beanName:{} ", bean.getClass(), beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifeCycle) {
            log.error("LifeCycle初始化之后调用[BeanPostProcessor.postProcessAfterInitialization] bean:{},beanName:{} ", bean.getClass(), beanName);
        }
        return bean;
    }

    /**
     * 使用BeanPostProcessor时必须实现PriorityOrdered接口，不然会拦截不到一些类，因为涉及到初始化顺序的问题
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
