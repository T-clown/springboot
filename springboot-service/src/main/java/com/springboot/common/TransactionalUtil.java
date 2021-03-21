package com.springboot.common;

import com.springboot.util.SpringContextHolder;

public class TransactionalUtil {

    private static volatile TransactionalComponent transactionalComponent;

    private static synchronized TransactionalComponent getTransactionalComponent() {
        if (transactionalComponent == null) {
            transactionalComponent = SpringContextHolder.getBean(TransactionalComponent.class);
        }
        return transactionalComponent;
    }

    public static void transactional(TransactionalComponent.Cell cell) {
        getTransactionalComponent().transactional(cell);
    }

}
