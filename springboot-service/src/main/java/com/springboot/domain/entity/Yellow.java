package com.springboot.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.springboot.common.serializer.BigDecimalScale;
import com.springboot.common.serializer.BigDecimalSerializer;
import com.springboot.common.serializer.ImageURL;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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

    @BigDecimalScale(scale = 2)
    private BigDecimal score = new BigDecimal("1.015");

    @BigDecimalScale(scale = 2, roundingMode = RoundingMode.HALF_DOWN)
    private BigDecimal score2 = new BigDecimal("1.015");

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal score3 = new BigDecimal("1.015");

    @ImageURL("img")
    //@JsonSerialize(using = ImageURLSerializer.class)
    private String img = "测试";
}
