package com.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @RestController是@ResponseBody和@Controller的组合注解。
 *
 * @SpringBootApplication：包含了@ComponentScan、@Configuration和@EnableAutoConfiguration注解。其中@ComponentScan让spring Boot扫描到Configuration类并把它加入到程序上下文
 *
 *Spring Boot建议将我们main方法所在的这个主要的配置类配置在根包名下
 *
 *https://blog.csdn.net/valada/article/details/80892573
 */
@SpringBootApplication
public class Application {
//update

    public static void main(String[] args) {
        /**
         * 该方法返回一个ApplicationContext对象，使用注解的时候返回的具体类型是AnnotationConfigApplicationContext或AnnotationConfigEmbeddedWebApplicationContext，当支持web的时候是第二个。
         */
        //启动方式1
        //SpringApplication.run(Application.class, args);
        //启动方式2
        //SpringApplication application = new SpringApplication(Application.class);
        //application.run(args);
        //启动方式3
        new SpringApplicationBuilder().sources(Application.class).run(args);
    }

}
