package com.springboot.common.extension;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Service;

/**
 * 4
 * BeanPostProcess接口只在bean的初始化阶段进行扩展（注入spring上下文前后），而InstantiationAwareBeanPostProcessor接口在此基础上增加了3
 * 个方法，把可扩展的范围增加了实例化阶段和属性注入阶段。
 *
 * 该类主要的扩展点有以下5个方法，主要在bean生命周期的两大阶段：实例化阶段和初始化阶段
 */
@Slf4j
@Service
public class TestInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor, PriorityOrdered {
    /**
     * 初始化bean之前，相当于把bean注入spring上下文之前
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof LifeCycle) {
            log.error("[TestInstantiationAwareBeanPostProcessor] before initialization " );
        }
        return bean;
    }

    /**
     * 初始化bean之后，相当于把bean注入spring上下文之后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof LifeCycle) {
            log.info("[TestInstantiationAwareBeanPostProcessor] after initialization " );
        }
        return bean;
    }

    /**
     * 实例化bean之前，相当于new这个bean之前
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if(beanClass .equals( LifeCycle.class)) {
            log.info("[TestInstantiationAwareBeanPostProcessor] before instantiation " );
        }
        return null;
    }

    /**
     * 实例化bean之后，相当于new这个bean之后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if(bean instanceof LifeCycle) {
            log.info("[TestInstantiationAwareBeanPostProcessor] after instantiation " );
        }
        return true;
    }
    /**
     * bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     * 给bean设置属性值
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if(bean instanceof LifeCycle) {
            log.error("[TestInstantiationAwareBeanPostProcessor] postProcessProperties " );
        }
        return InstantiationAwareBeanPostProcessor.super.postProcessProperties(pvs, bean, beanName);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
