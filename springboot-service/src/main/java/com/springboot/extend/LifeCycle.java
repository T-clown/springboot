package com.springboot.extend;

import com.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.awt.print.Book;


/**
 * Bean的生命周期
 */
@Service("lifeCycle2")
@Slf4j
public class LifeCycle implements
        BeanNameAware,
        BeanFactoryAware,
        ApplicationContextAware,
        BeanPostProcessor,
        InitializingBean,
        DisposableBean {


    private UserService userService;

    public LifeCycle() {
        log.error("第一步：LifeCycle对象初始化，执行无参构造方法,userService:{} ", userService);
    }

    public LifeCycle(UserService userService) {
        this.userService=userService;
        log.error("第一步：LifeCycle对象初始化，执行有参构造方法,userService:{} ", userService);
    }

    /**
     * setter注入
     *
     * @param userService
     */
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
        log.error("第二步：执行LifeCycle的set方法给userService赋值，userService: {}", userService);
    }

    @Override
    public void setBeanName(String name) {
        log.error("第三步：LifeCycle.setBeanName invoke,userService: {},name:{}", userService,name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        log.error("第四步：设置BeanFactory容器属性，LifeCycle.setBeanFactory invoke,userService: {},beanFactory:{}", userService,beanFactory.getClass());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.error("第五步：设置ApplicationContext容器属性，LifeCycle.setApplicationContext invoke,applicationContext:{},userService: {}",applicationContext.getApplicationName(), userService);
    }

    // 自定义初始化方法
    @PostConstruct
    public void init() {
        log.error("第六步：执行LifeCycle的@PostConstruct,userService: {}", userService);
    }


    @Override
    public void afterPropertiesSet() {
        log.error("第七步：LifeCycle.afterPropertiesSet invoke,userService: {}", userService);
    }


    public UserService getUserService() {
        log.error("第九步：使用bean");
        return userService;
    }

    // 自定义销毁方法
    @PreDestroy
    public void springPreDestory() {
        log.error("第十步：执行LifeCycle的@PreDestory");
    }

    @Override
    public void destroy() {
        System.out.println("第十一步：LifeCycle.destory invoke");
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifeCycle) {
            log.error("LifeCycle.postProcessBeforeInitialization bean:{},beanName:{} ", bean.getClass(), beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LifeCycle) {
            log.error("LifeCycle.postProcessAfterInitialization bean:{},beanName:{} ", bean.getClass(), beanName);
        }
        return bean;
    }


}