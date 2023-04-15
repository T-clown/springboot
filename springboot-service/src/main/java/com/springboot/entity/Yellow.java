package com.springboot.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.springboot.serializer.BigDecimalScale;
import com.springboot.serializer.BigDecimalSerializer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Data
//@PropertySource(value = "classpath:application.properties",encoding = "UTF-8")
public class Yellow implements Serializable {
    private Integer id;
    @Value("${com.properties.name}")
    private String name;
    @Value("${com.properties.title}")
    private String title;

    @JsonSerialize(using = BigDecimalSerializer.class)
    @BigDecimalScale(scale = 2)
    private BigDecimal score=new BigDecimal("1.015");

    @JsonSerialize(using = BigDecimalSerializer.class)
    @BigDecimalScale(scale = 2,roundingMode = RoundingMode.HALF_DOWN)
    private BigDecimal score2=new BigDecimal("1.015");

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal score3=new BigDecimal("1.015");
}
