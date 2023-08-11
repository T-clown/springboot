package com.springboot.common.serializer;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * @author ti
 */
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = BigDecimalSerializer.class)
public @interface BigDecimalScale {
    /**
     * scale
     * @return
     */
    int scale() default 1;

    /**
     * RoundingMode
     * @return
     */
    RoundingMode roundingMode() default RoundingMode.HALF_UP;
}
