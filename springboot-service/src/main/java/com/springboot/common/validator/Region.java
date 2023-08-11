package com.springboot.common.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自定义注解实现参数校验
 * @author ti
 */
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = RegionValidator.class)
@Documented
public @interface Region {

    /**
     * 错误信息
     * @return
     */
    String message() default "Region 值不在可选范围内";

    /**
     * 验证组
     * @return
     */
    Class<?>[] groups() default {};

    /**
     *
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
