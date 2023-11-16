package com.springboot.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Slf4j
public class DTSMybatisInterceptor implements Interceptor {

    private static Set<String> dtsTables;

    private static String env;

    private static String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public DTSMybatisInterceptor(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${mybatis.dts.tables:}")
    public void setDtsTables(String dtsTables) {
        String[] split = dtsTables.split(",");
        DTSMybatisInterceptor.dtsTables = Arrays.stream(split).collect(Collectors.toSet());
    }

    @Value("${spring.kafka.topics.dts:}")
    public void setTopic(String topic) {
        DTSMybatisInterceptor.topic = topic;
    }

    @Value("${mybatis.dts.env:dev}")
    public void setDtsEnv(String env) {
        DTSMybatisInterceptor.env = env;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        Configuration configuration = ms.getConfiguration();
        BoundSql boundSql = ms.getBoundSql(parameter);
        Set<String> tableNames = getTableNames(boundSql.getSql());
        boolean matchDts = tableNames.stream().anyMatch(tableName -> dtsTables.contains(tableName));
        if (!matchDts) {
            return invocation.proceed();
        }

        String sql = showSql(configuration, boundSql);
        log.info("执行SQL[{}]", sql);

        Object res = invocation.proceed();
        // send dts
        DTSMessage dtsMessage = new DTSMessage();
        dtsMessage.setEnv(env);
        dtsMessage.setSql(sql);
        dtsMessage.setTableNames(tableNames);
        String messageStr = JSON.toJSONString(dtsMessage);
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            log.info("DTS condition matched, pending message: {}, topic={}", messageStr, topic);
            TransactionSynchronizationManager.registerSynchronization(new DTSTransactionHandler(messageStr, topic));
        } else {
            log.info("DTS condition matched, sending dts message: {}, topic={}", messageStr, topic);
            kafkaTemplate.send(topic, messageStr);
        }
        return res;
    }

    private Set<String> getTableNames(String sql) {
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            return Collections.emptySet();
        }
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List<String> tableList = tablesNamesFinder.getTableList(statement);
        Set<String> tableNames = new HashSet<>();
        for (String tableName : tableList) {
            //获取去掉“`”的表名
            if (tableName.startsWith("`") && tableName.endsWith("`")) {
                tableNames.add(tableName.substring(1, tableName.length() - 1));
            } else {
                tableNames.add(tableName);
            }
        }

        return tableNames;
    }

    private static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // sql语句中多个空格都用一个空格代替
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?",
                        Matcher.quoteReplacement(getParameterValue(parameterObject)));

            } else {
                // MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    //fixme 找出为什么replaceFirst方法占用CPU很高
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?",
                                Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
