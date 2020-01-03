package com.springboot.config;

import java.util.Properties;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 分页配置（另一种是在yml里面配置）
 * 这里需要注意的是reasonable参数，表示分页合理化，默认值为false。
 * 如果该参数设置为 true 时，pageNum<=0 时会查询第一页，pageNum>pages（超过总数时），会查询最后一页。
 * 默认false 时，直接根据参数进行查询
 */
@Component
public class PageHelperConfig {
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //数据库
        properties.setProperty("helperDialect", "mysql");
        //是否将参数offset作为PageNum使用
        properties.setProperty("offsetAsPageNum", "true");
        //是否进行count查询
        properties.setProperty("rowBoundsWithCount", "true");
        //是否分页合理化
        properties.setProperty("reasonable", "false");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
}
