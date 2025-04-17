package com.springboot.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author macbookpro
 */
public class ValidationUtil {

    private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
    private static final Validator VALIDATOR = FACTORY.getValidator();

    public static <T> String validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
        return violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));
    }
}
