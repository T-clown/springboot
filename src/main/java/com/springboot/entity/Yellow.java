package com.springboot.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//@PropertySource(value = "classpath:application.properties",encoding = "UTF-8")
public class Yellow {
    private Integer id;
    @Value("${com.properties.name}")
    private String name;
    @Value("${com.properties.title}")
    private String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
