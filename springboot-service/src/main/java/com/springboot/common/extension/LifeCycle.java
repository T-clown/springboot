package com.springboot.common.extension;

import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringValueResolver;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Bean的生命周期
 *
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.EnvironmentAware#setEnvironment
 * @see org.springframework.context.EmbeddedValueResolverAware#setEmbeddedValueResolver
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor#postProcessBeforeDestruction
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 */
@Service("lifeCycle2")
@Slf4j
public class LifeCycle implements
        BeanNameAware,
        BeanClassLoaderAware,
        BeanFactoryAware,
        EnvironmentAware,
        EmbeddedValueResolverAware,
        ResourceLoaderAware,
        ApplicationEventPublisherAware,
        MessageSourceAware,
        ApplicationContextAware,
        ServletContextAware,
        InitializingBean,
        DestructionAwareBeanPostProcessor,
        DisposableBean  {


    private ClassLoader classLoader;
    private ApplicationEventPublisher applicationEventPublisher;
    private BeanFactory beanFactory;
    private StringValueResolver stringValueResolver;
    private Environment environment;
    private MessageSource messageSource;
    private ResourceLoader resourceLoader;
    private ServletContext servletContext;
    private ApplicationContext applicationContext;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        log.info("LifeCycle[BeanClassLoaderAware.setBeanClassLoader]");
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        log.info("LifeCycle[DestructionAwareBeanPostProcessor.postProcessBeforeDestruction,bean:{},beanName:{}]", bean.getClass(), beanName);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
        log.info("LifeCycle[ApplicationEventPublisherAware.setApplicationEventPublisher]");
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.stringValueResolver = resolver;
        log.info("LifeCycle[EmbeddedValueResolverAware.setEmbeddedValueResolver]");
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        log.info("LifeCycle[EnvironmentAware.setEnvironment]");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        log.info("LifeCycle[MessageSourceAware.setMessageSource]");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        log.info("LifeCycle[ResourceLoaderAware.setResourceLoader]");
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        log.info("LifeCycle[ServletContextAware.setServletContext]");
    }


    //private UserService userService;

    public LifeCycle() {
        log.error("第一步：LifeCycle对象初始化，执行无参构造方法 ");
    }

//    public LifeCycle(UserService userService) {
//        this.userService = userService;
//        log.error("第一步：LifeCycle对象初始化，执行有参构造方法,userService:{} ", userService);
//    }

    /**
     * setter注入
     *
     * @param userService
     */
//    @Autowired
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//        log.error("第二步：执行LifeCycle的set方法给userService赋值，userService: {}", userService);
//    }

    @Override
    public void setBeanName(String name) {
        log.error("第三步：LifeCycle[BeanNameAware.setBeanName],name:{}", name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        log.error("第四步：设置BeanFactory容器属性，LifeCycle[BeanFactoryAware.setBeanFactory] ,beanFactory:{}", beanFactory.getClass());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        log.error("第五步：设置ApplicationContext容器属性，LifeCycle[ApplicationContextAware.setApplicationContext] ,applicationContext:{}", applicationContext.getApplicationName());
    }

    // 自定义初始化方法
    @PostConstruct
    public void init() {
        log.error("第六步：执行LifeCycle的@PostConstruct");
    }


    @Override
    public void afterPropertiesSet() {
        log.error("第七步：LifeCycle[InitializingBean.afterPropertiesSet]");
    }


    /**
     * 自定义销毁方法
     */
    @PreDestroy
    public void preDestroy() {
        log.error("第十步：执行LifeCycle的@PreDestory");
    }

    @Override
    public void destroy() {
        log.info("第十一步：LifeCycle[DisposableBean.destroy] ");
    }



}