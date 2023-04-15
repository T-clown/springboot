package com.springboot.serializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
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
