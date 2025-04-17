package com.springboot.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author macbookpro
 */
public class TrimValidator implements ConstraintValidator<Trim, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            // 对于 null 值不处理，交给其他校验规则
            return true;
        }
        // 去掉两边空格并验证是否满足特定条件
        // 非空校验
        return !value.trim().isEmpty();
    }
}
