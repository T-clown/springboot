package com.springboot.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.fastjson.JSON;

import com.springboot.common.exception.ServiceRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * https://gitee.com/yintianwen7/spring-dynamic-datasource
 * https://juejin.im/post/5d8705e65188253f4b629f47
 * 动态数据源
 */
@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private static Map<Object, Object> targetDataSources = new HashMap<>();

    public DynamicRoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
        DynamicRoutingDataSource.targetDataSources = targetDataSources;
    }

    /**
     * 设置当前数据源
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        log.info("Current DataSource is [{}]", DynamicDataSourceContextHolder.getDataSourceKey());
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        DynamicRoutingDataSource.targetDataSources = targetDataSources;
    }

    /**
     * 是否存在当前key的 DataSource
     *
     * @param key
     * @return 存在返回 true, 不存在返回 false
     */
    public static boolean isExistDataSource(String key) {
        return targetDataSources.containsKey(key);
    }

    /**
     * 动态增加数据源
     *
     * @param dataSourceMap 数据源属性
     * @return
     */
    public synchronized boolean addDataSource(Map<String, String> dataSourceMap) {
        pingConnection(dataSourceMap);
        try {
            if (DynamicRoutingDataSource.isExistDataSource(dataSourceMap.get(DruidDataSourceFactory.PROP_URL))) {
                return true;
            }
            DruidDataSource druidDataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(dataSourceMap);
            druidDataSource.init();
            DynamicRoutingDataSource.targetDataSources.put(dataSourceMap.get(DruidDataSourceFactory.PROP_URL),
                druidDataSource);
            this.afterPropertiesSet();
        } catch (Exception e) {
            log.error("添加数据源失败,dataSource:{},e:{}", JSON.toJSONString(dataSourceMap), e.getMessage());
            return false;
        }
        return true;
    }

    private static void pingConnection(Map<String, String> dataSourceMap) {
        Connection connection = null;
        // 排除连接不上的错误
        try {
            Class.forName(dataSourceMap.get(DruidDataSourceFactory.PROP_DRIVERCLASSNAME));
            connection = DriverManager.getConnection(
                dataSourceMap.get(DruidDataSourceFactory.PROP_URL),
                dataSourceMap.get(DruidDataSourceFactory.PROP_USERNAME),
                dataSourceMap.get(DruidDataSourceFactory.PROP_PASSWORD));
        } catch (Exception e) {
            log.error("数据源连接失败，dataSource:{}", JSON.toJSONString(dataSourceMap));
            throw new ServiceRuntimeException("数据源连接失败");
        } finally {
            try {
                if (connection != null) { connection.close(); }
            } catch (SQLException e) {
                log.error("数据源连接关闭失败，dataSource:{}", JSON.toJSONString(dataSourceMap));
            }
        }
    }

}
