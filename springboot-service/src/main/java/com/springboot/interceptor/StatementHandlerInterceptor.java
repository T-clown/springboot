package com.springboot.interceptor;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;

import cn.hutool.core.util.ReflectUtil;
import com.springboot.common.enums.CommonYN;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * 拦截Sql语法构建的处理
 */
@Slf4j
@Intercepts(
        value = {@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class StatementHandlerInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        Object parameterObject = boundSql.getParameterObject();
        String sql = boundSql.getSql();
        if (sql.trim().toUpperCase().startsWith("UPDATE")) {
            ReflectUtil.setFieldValue(parameterObject, "updateTime", LocalDateTime.now());
            ReflectUtil.setFieldValue(boundSql, "parameterObject", parameterObject);
        }
        return invocation.proceed();
    }

    private void insertSetValue(Object obj) {
        ReflectUtil.setFieldValue(obj, "createTime", LocalDateTime.now());
        ReflectUtil.setFieldValue(obj, "updateTime", LocalDateTime.now());
        ReflectUtil.setFieldValue(obj, "isDeleted", CommonYN.NO.value());
    }


    @Override
    public Object plugin(Object target) {
        log.info("拦截的类：{}", target.getClass().getName());
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        log.info("properties:{}", properties);
    }
}
