
package com.springboot.common.spi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Inject {
    // whether enable injection or not
    boolean enable() default true;

    // inject type default by name injection
    InjectType type() default InjectType.ByName;

    enum InjectType{
        /**
         *
         */
        ByName,
        ByType
    }
}
