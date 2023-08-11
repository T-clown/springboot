package com.springboot.domain.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix="p")
@Data
public class Phone {
    private int id;
    private String name;
    private BigDecimal price;
    private String cacheName;
    private Date dateInProduced;

    public static String cache;

    @PostConstruct
    public void init() {
        cache=cacheName;
    }
}
