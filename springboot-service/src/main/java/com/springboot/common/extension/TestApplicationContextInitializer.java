package com.springboot.common.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 1
 * 这是整个spring容器在刷新之前初始化ConfigurableApplicationContext的回调接口，
 * 简单来说，就是在容器刷新之前调用此类的initialize方法。这个点允许被用户自己扩展。
 * 用户可以在整个spring容器还没被初始化之前做一些事情。
 *
 * ApplicationContextInitializer 的实现有三种
 * 第一种是在 classpath 路径下的 META-INF/spring.factories 文件中填写接口和实现类的全名，多个实现的话用逗号分隔。
 * 第二种是在 Spring Boot 启动代码中手动添加初始化器
 * 第三种是在 application.properties 中配置 context.initializer.classes
 *
 */
@Slf4j
public class TestApplicationContextInitializer implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.error("步骤1：[ApplicationContextInitializer]");
    }

}