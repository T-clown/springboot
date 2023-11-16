package com.springboot.common.extension;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Service;

/**
 * 可以看到，这个类也是Aware扩展的一种，触发点在bean的初始化之前，也就是postProcessBeforeInitialization之前，这个类的触发点方法只有一个：setBeanName
 * 使用场景为：用户可以扩展这个点，在初始化bean之前拿到spring容器中注册的的beanName，来自行修改这个beanName的值。
 */
@Slf4j
@Service
public class TestBeanNameAware implements BeanNameAware {


    public TestBeanNameAware() {
        log.error("NormalBean 执行构造方法");
    }

    @Override
    public void setBeanName(String name) {
        log.error("[BeanNameAware  属性赋值] " + name);
    }

    /**
     * 这个并不算一个扩展点，其实就是一个标注。其作用是在bean的初始化阶段，如果对一个方法标注了@PostConstruct
     * ，会先调用这个方法。这里重点是要关注下这个标准的触发点，这个触发点是在postProcessBeforeInitialization之后，InitializingBean.afterPropertiesSet之前。
     *
     * 使用场景：用户可以对某一方法进行标注，来进行初始化某一个属性
     */
    @PostConstruct
    public void init(){
        log.error("[PostConstruct] TestBeanNameAware");
    }

}
