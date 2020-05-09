package com.springboot.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "master.datasource")
public class DataSourceInfo {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public Map<String, String> getProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("driverClassName", this.getDriverClassName());
        map.put("url", this.getUrl());
        map.put("username", this.getUsername());
        map.put("password", this.getPassword());
        return map;
    }
}
