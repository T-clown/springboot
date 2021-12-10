package com.springboot.handler;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class CallBackService {
    /**
     * 事务提交后执行
     * @param action
     */
    public void execute(final CallBack action) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager
                    .registerSynchronization(new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            // 事务提交后执行回调
                            action.callback();
                        }
                    });
        } else {
            // 事务提交后执行回调
            action.callback();
        }

    }
}
