package com.springboot.utils.trransaction;

import java.lang.annotation.*;

/**
 * @author macbookpro
 * @description: 事务提交注解
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TransactionCommitCallback {
    /**
     * 是否异步执行
     *
     * @return
     */
    boolean async() default false;
}
