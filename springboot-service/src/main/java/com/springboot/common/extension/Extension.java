package com.springboot.common.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Extension implements
        CommandLineRunner,
        ApplicationRunner,
        BeanFactoryPostProcessor,
        SmartInitializingSingleton,
        BeanFactoryAware,
        InitializingBean {

    /**
     * args数组包含启动应用程序时传递的命令行参数
     * 这个接口也只有一个方法：run(String… args)，触发时机为整个项目启动完毕后，自动执行。
     * 如果有多个CommandLineRunner，可以利用@Order来进行排序。
     * 使用场景：用户扩展此接口，进行启动项目之后一些业务的预处理。
     * 用于不需要访问详细命令行参数和选项的场景，例如执行一些初始化任务或加载初始数据
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        log.error("[CommandLineRunner]");
    }

    /**
     * 用于需要访问命令行参数和选项的场景，例如需要执行一些基于命令行参数的初始化逻辑
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.error("[ApplicationRunner]");
    }


    /**
     * 3
     * 这个接口是beanFactory的扩展接口，调用时机在spring在读取beanDefinition信息之后，实例化bean之前。
     * 在这个时机，用户可以通过实现这个扩展接口来自行处理一些东西，比如修改已经注册的beanDefinition的元信息。
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.error("[BeanFactoryPostProcessor]");
    }

    /**
     * 这个接口中只有一个方法afterSingletonsInstantiated
     * 其作用是是在spring容器管理的所有单例对象（非懒加载对象）初始化完成之后调用的回调接口。
     * 其触发时机为postProcessAfterInitialization之后。
     * <p>
     * 使用场景：用户可以扩展此接口在对所有单例对象初始化完毕后，做一些后置的业务处理。
     */
    @Override
    public void afterSingletonsInstantiated() {
        log.error("[SmartInitializingSingleton]");
    }


    private BeanFactory beanFactory;

    /**
     * 6
     * 这个类只有一个触发点，发生在bean的实例化之后，注入属性之前，也就是Setter之前。这个类的扩展点方法为setBeanFactory，可以拿到BeanFactory这个属性。
     * 使用场景为，你可以在bean实例化之后，但还未初始化之前，拿到 BeanFactory，在这个时候，可以对每个bean作特殊化的定制。也或者可以把BeanFactory拿到进行缓存，日后使用。
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        log.error("[BeanFactoryAware]");
    }


    /**
     * 这个类，顾名思义，也是用来初始化bean的。InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，凡是继承该接口的类，在初始化bean
     * 的时候都会执行该方法。这个扩展点的触发时机在postProcessAfterInitialization之前。
     * <p>
     * 使用场景：用户实现此接口，来进行系统启动的时候一些业务指标的初始化工作。
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.error("[InitializingBean]");
    }

}
