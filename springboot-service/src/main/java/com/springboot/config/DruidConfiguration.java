//package com.springboot.config;
//
//import com.alibaba.druid.DbType;
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//
//import com.alibaba.druid.wall.WallConfig;
//import com.alibaba.druid.wall.WallFilter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//
//@Slf4j
//@Configuration
//public class DruidConfiguration {
//
//    @Bean
//    public ServletRegistrationBean druidStatViewServle() {
//        //注册服务
//        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(
//            new StatViewServlet(), "/druid/*");
//        // 白名单(为空表示,所有的都可以访问,多个IP的时候用逗号隔开)
//        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
//        // IP黑名单 (存在共同时，deny优先于allow)
//        servletRegistrationBean.addInitParameter("deny", "127.0.0.2");
//        // 设置登录的用户名和密码
//        servletRegistrationBean.addInitParameter("loginUsername", "admin");
//        servletRegistrationBean.addInitParameter("loginPassword", "123456");
//        // 是否能够重置数据.
//        servletRegistrationBean.addInitParameter("resetEnable", "false");
//        return servletRegistrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean druidStatFilter() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
//        // 添加过滤规则
//        filterRegistrationBean.addUrlPatterns("/*");
//        // 添加不需要忽略的格式信息
//        filterRegistrationBean.addInitParameter("exclusions",
//            "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
//        log.info("druid初始化成功!");
//        return filterRegistrationBean;
//
//    }
//
//    @Bean(name = "wallFilter")
//    @DependsOn("wallConfig")
//    public WallFilter wallFilter(WallConfig wallConfig) {
//        WallFilter wallFilter = new WallFilter();
//        wallFilter.setConfig(wallConfig);
//        wallFilter.setDbType(DbType.mysql);
//        return wallFilter;
//    }
//
//    @Bean(name = "wallConfig")
//    public WallConfig wallConfig() {
//        WallConfig wallConfig = new WallConfig();
//        //允许一次执行多条语句
//        wallConfig.setMultiStatementAllow(true);
//        //允许一次执行多条语句
//        wallConfig.setNoneBaseStatementAllow(true);
//        //是否进行严格的语法检测，Druid SQL Parser在某些场景不能覆盖所有的SQL语法，出现解析SQL出错，可以临时把这个选项设置为false，同时把SQL反馈给Druid的开发者。
//        wallConfig.setStrictSyntaxCheck(false);
//        return wallConfig;
//    }
//
//}
