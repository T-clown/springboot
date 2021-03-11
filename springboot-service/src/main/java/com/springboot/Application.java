package com.springboot;

import com.springboot.extend.TestApplicationContextInitializer;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @RestController是@ResponseBody和@Controller的组合注解。
 * @SpringBootApplication：包含了@ComponentScan、@Configuration和@EnableAutoConfiguration注解。其中@ComponentScan让spring Boot扫描到Configuration类并把它加入到程序上下文
 *
 * Spring Boot建议将我们main方法所在的这个主要的配置类配置在根包名下
 *
 * https://blog.csdn.net/valada/article/details/80892573
 * @ImportResource注解用于导入Spring的配置文件，让配置文件里面的内容生效，这个注解是放在主入口函数的类上
 */
//@Import({DynamicDataSourceConfig.class})
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
        //SpringApplication.run(Application.class, args);
        //启动方式2
        SpringApplication application = new SpringApplication(Application.class);
        application.addInitializers(new TestApplicationContextInitializer());
        application.run(args);
        //启动方式3
        //new SpringApplicationBuilder().sources(Application.class).run(args);
    }

}
