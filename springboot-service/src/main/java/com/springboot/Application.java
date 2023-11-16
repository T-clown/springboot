package com.springboot;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.github.dreamroute.sqlprinter.starter.anno.EnableSQLPrinter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @EnableCircuitBreaker - 开启断路器。就是开启hystrix服务容错能力。
 * 当应用启用Hystrix服务容错的时候，必须增加的一个注解。
 */
@Slf4j
//@Import({DynamicDataSourceConfig.class})
//@EnableWebMvc
//@EnableHystrix
//@ServletComponentScan
//@EnableSQLPrinter
@NacosPropertySource(dataId = "springboot", autoRefreshed = true)
@EnableDynamicThreadPool
@EnableDubbo(scanBasePackages = "com.springboot.client")
@EnableAsync
@MapperScan(basePackages = {"com.springboot.dao"})
@SpringBootApplication(scanBasePackages = "com.springboot", exclude = DataSourceAutoConfiguration.class)
public class Application {
    public static void main(String[] args) throws UnknownHostException {
        /**
         * 该方法返回一个ApplicationContext对象，使用注解的时候返回的具体类型是AnnotationConfigApplicationContext
         * 或AnnotationConfigEmbeddedWebApplicationContext，当支持web的时候是第二个。
         */
        //启动方式1
        // SpringApplication.run(Application.class, args);

        //启动方式2
//        SpringApplication application = new SpringApplication(Application.class);
//        application.addInitializers(new TestApplicationContextInitializer());
//        application.run(args);
        //启动方式3
        //new SpringApplicationBuilder().sources(Application.class).run(args);

        SpringApplication app = new SpringApplication(Application.class);
        ConfigurableApplicationContext application = app.run(args);
        //ConfigurableApplicationContext application=SpringApplication.run(Knife4jSpringBootDemoApplication.class, args);
        Environment env = application.getEnvironment();
        log.info("""
                        
                      \n----------------------------------------------------------
                        \tApplication '{}' is running! Access URLs:
                        \tLocal: \t\thttp://localhost:{}
                        \tExternal: \thttp://{}:{}
                        \tDoc: \thttp://{}:{}{}/doc.html
                        ----------------------------------------------------------""",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));

        log.info("""
               \n----------------------------------------------------------
                \tnacos地址:{}
                ----------------------------------------------------------
                """, "http://localhost:8848/nacos");
    }

}
