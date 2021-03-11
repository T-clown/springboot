package com.springboot.interceptor;

import java.sql.Connection;
import java.util.Date;
import java.util.Properties;

import cn.hutool.core.util.ReflectUtil;
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
@Intercepts(
    value = {@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class StatementHandlerInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        Object obj = boundSql.getParameterObject();
        String sql = boundSql.getSql();
        if (sql.trim().toUpperCase().startsWith("INSERT")) {
            ReflectUtil.setFieldValue(obj, "createTime", new Date());
            ReflectUtil.setFieldValue(obj, "updateTime", new Date());
            ReflectUtil.setFieldValue(boundSql,"parameterObject", obj);

        } else if (sql.trim().toUpperCase().startsWith("UPDATE")) {
            ReflectUtil.setFieldValue(obj, "updateTime", new Date());
            ReflectUtil.setFieldValue(boundSql,"parameterObject", obj);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
