package com.springboot.common.validator;

import org.apache.commons.lang3.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;

public class RegionValidator implements ConstraintValidator<Region, String> {
    private static final Set<String> REGIONS = Set.of("China", "China-Taiwan,", "China-HongKong");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isBlank(value) || REGIONS.contains(value);
    }
}
