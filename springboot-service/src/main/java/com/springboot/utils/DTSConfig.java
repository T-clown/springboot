package com.springboot.utils;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class DTSConfig {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final List<SqlSessionFactory> sqlSessionFactories;

    public DTSConfig(KafkaTemplate<String, String> kafkaTemplate, List<SqlSessionFactory> sqlSessionFactories) {
        this.kafkaTemplate = kafkaTemplate;
        this.sqlSessionFactories = sqlSessionFactories;
    }
    /**
     * 添加mybatis拦截器方式之一
     * 另外两种添加方式是直接加上@Component注解或xml文件里面配置
     */
//    @PostConstruct
//    public void addDTSMyBatisInterceptor() {
//        DTSMybatisInterceptor dtsMybatisInterceptor = new DTSMybatisInterceptor(kafkaTemplate);
//        sqlSessionFactories.forEach(sqlSessionFactory -> sqlSessionFactory.getConfiguration().addInterceptor(dtsMybatisInterceptor));
//    }
}
