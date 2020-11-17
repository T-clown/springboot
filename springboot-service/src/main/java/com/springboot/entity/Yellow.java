package com.springboot.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
//@PropertySource(value = "classpath:application.properties",encoding = "UTF-8")
public class Yellow {
    private Integer id;
    @Value("${com.properties.name}")
    private String name;
    @Value("${com.properties.title}")
    private String title;
}
