package com.springboot;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @EnableCircuitBreaker - 开启断路器。就是开启hystrix服务容错能力。
 * 当应用启用Hystrix服务容错的时候，必须增加的一个注解。
 */
//@Import({DynamicDataSourceConfig.class})
//@EnableWebMvc
//@EnableHystrix
@EnableDynamicThreadPool
@EnableAsync
@MapperScan(basePackages = {"com.springboot.dao"})
@SpringBootApplication(scanBasePackages = "com.springboot", exclude = DataSourceAutoConfiguration.class)
public class Application {
    public static void main(String[] args) {
        /**
         * 该方法返回一个ApplicationContext对象，使用注解的时候返回的具体类型是AnnotationConfigApplicationContext
         * 或AnnotationConfigEmbeddedWebApplicationContext，当支持web的时候是第二个。
         */
        //启动方式1
        SpringApplication.run(Application.class, args);

        //启动方式2
//        SpringApplication application = new SpringApplication(Application.class);
//        application.addInitializers(new TestApplicationContextInitializer());
//        application.run(args);
        //启动方式3
        //new SpringApplicationBuilder().sources(Application.class).run(args);
    }

}
