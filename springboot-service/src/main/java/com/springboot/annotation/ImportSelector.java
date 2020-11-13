package com.springboot.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.springboot.common.enums.CommonYN;
import com.springboot.service.PayConfigurationSelector;
import org.springframework.context.annotation.Import;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PayConfigurationSelector.class)
public @interface ImportSelector {

    Class<? extends Annotation> annotation() default Annotation.class;

    CommonYN mode() default CommonYN.YES;
}
