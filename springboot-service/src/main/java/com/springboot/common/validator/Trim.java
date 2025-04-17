package com.springboot.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author macbookpro
 */
@Documented
@Constraint(validatedBy = TrimValidator.class) // 指定校验器
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Trim {
    /**
     * 默认错误消息
     *
     * @return
     */
    String message() default "Invalid input";

    /**
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
