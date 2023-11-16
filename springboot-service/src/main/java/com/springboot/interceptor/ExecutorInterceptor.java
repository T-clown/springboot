package com.springboot.interceptor;

import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 这里可以定义多个@Signature对多个地方拦截，都用这个拦截器
 * 拦截执行器的方法
 * 注册拦截器的三种方式：
 * 1.拦截器上添加@Component注解
 * 2.通过@Bean注解创建拦截器并交给Spring管理
 * 3.mybatis的xml配置文件里配置
 * 坑点：重复配置会多次注册拦截器
 */
@Slf4j
@Intercepts({
        @Signature(type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class ExecutorInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        log.info("拦截的类：{}", target.getClass().getName());
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
