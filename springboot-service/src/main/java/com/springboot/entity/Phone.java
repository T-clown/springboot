package com.springboot.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="p")
@Data
public class Phone {
    private int id;
    private String name;
    private BigDecimal price;
    private Date dateInProduced;
}
