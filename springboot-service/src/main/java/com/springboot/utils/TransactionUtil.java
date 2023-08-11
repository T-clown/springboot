package com.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import jakarta.annotation.PostConstruct;

@Slf4j
@Component
public class TransactionUtil implements InitializingBean {

    @Autowired
    private TransactionTemplate transactionTemplate;

    private static TransactionTemplate template;


    @PostConstruct
    public void init(){
        log.info("TransactionTemplate初始化");
        template=transactionTemplate;
    }
//    /**
//     * 加锁调用
//     * @param callable
//     * @param params
//     * @param <T>
//     * @return
//     */
//    public static <T> T callLocked(Callable<T> callable, TransactionDefinition definition) {
//        transactionTemplate.setPropagationBehavior();
//    }


    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("afterPropertiesSet");
    }
}
