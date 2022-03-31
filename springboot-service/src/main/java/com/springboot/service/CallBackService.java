package com.springboot.service;

import com.springboot.handler.CallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 事务提交后执行
 */
@Slf4j
@Component
public class CallBackService {

    public void execute(final CallBack action) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // 事务提交后执行回调，如果回调的时候需要事务，则回调事务用PROPAGATION_REQUIRES_NEW
                    action.callback();
                }

                @Override
                public void suspend() {
                    //暂停TransactionSynchronizationManager管理的资源
                }

                @Override
                public void resume() {
                    //恢复TransactionSynchronizationManager管理的资源
                }

                @Override
                public void flush() {
                }

                @Override
                public void beforeCommit(boolean readOnly) {
                    //这里发生异常会传播给调用者并导致事务回滚
                    log.info("beforeCommit：事务提交前执行，事务即便回魂也会执行，readOnly:{}",readOnly);
                }

                @Override
                public void beforeCompletion() {
                    //这里发生异常不会传播给调用者
                    log.info("beforeCompletion：事务完成前（提交或者回滚）执行，一般用于关闭资源");
                }

                @Override
                public void afterCompletion(int status) {
                    //这里发生异常不会传播给调用者
                    log.info("afterCompletion：事务完成后（提交或者回滚）执行，一般用于关闭资源，status:{}",status);
                }
            });
        } else {
            // 事务提交后执行回调
            log.info("无事务执行回调");
            action.callback();
        }

    }

}