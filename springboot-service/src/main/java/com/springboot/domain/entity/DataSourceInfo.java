package com.springboot.domain.entity;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Data
@Component
@ConfigurationProperties(prefix = "cluster.datasource")
public class DataSourceInfo {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public Map<String, String> getProperties() {
        Map<String, String> result= Maps.newHashMap();
        DataSourceInfo dataSourceInfo=this;
        Map<String, Object> dataMap = BeanUtil.beanToMap(dataSourceInfo);
        Optional.ofNullable(dataMap).ifPresent(x->x.forEach((k,v)->result.put(k,String.valueOf(v))));
        return result;
    }
}
