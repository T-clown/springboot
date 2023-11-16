package com.springboot.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static <T> String validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        return violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
    }
}
