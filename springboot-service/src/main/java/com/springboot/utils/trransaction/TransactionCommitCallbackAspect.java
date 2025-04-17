package com.springboot.utils.trransaction;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author macbookpro
 * @description: 提交事务切面
 **/
@Aspect
@Component
@Slf4j
public class TransactionCommitCallbackAspect {
    @Resource
    private TransactionCommitCallbackService transactionCommitCallbackService;

    @Around("@annotation(transactionCommitCallBack)")
    public Object around(ProceedingJoinPoint joinPoint, TransactionCommitCallback transactionCommitCallBack) {

        boolean async = transactionCommitCallBack.async();

//        transactionCommitCallbackService.execute(() -> {
//            try {
//                joinPoint.proceed();
//            } catch (Throwable throwable) {
//                log.error("submit thread error:{}", throwable.getMessage(), throwable);
//            }
//        });
        ExecuteUtilV2.execute(() -> {
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                log.error("submit thread error:{}", throwable.getMessage(), throwable);
            }
        }, async);
        return null;
    }

    ;


}
