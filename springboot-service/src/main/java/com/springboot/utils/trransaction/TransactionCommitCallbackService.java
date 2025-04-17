package com.springboot.utils.trransaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author macbookpro
 */
@Slf4j
@Component
public class TransactionCommitCallbackService implements TransactionSynchronization, Executor {
    /**
     * 用于存放同个事务中多个执行事件
     */
    private final ThreadLocal<List<Runnable>> EVENT_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(Runnable runnable) {
        boolean isTransactionActive = registerSynchronizationActiveTransaction(runnable);
        //如果处于非事务状态，则直接运行
        if (!isTransactionActive) {
            log.warn("No transaction is active");
            runnable.run();
        }
    }

    @Override
    public void afterCommit() {
        processEvent();
    }

    @Override
    public void afterCompletion(int status) {
        EVENT_THREAD_LOCAL.remove();
    }

    /**
     * 事件处理
     */
    private void processEvent() {
        log.info("processEvent after transaction commit");
        List<Runnable> eventList = EVENT_THREAD_LOCAL.get();
        try {
            for (Runnable runnable : eventList) {
                runnable.run();
            }
        } catch (Exception e) {
            log.error("afterCommit error :{} ", e.getMessage(), e);
        }
    }

    /**
     * 注册处于活动状态的事务
     *
     * @param runnable
     * @return
     */
    private boolean registerSynchronizationActiveTransaction(Runnable runnable) {
        boolean transactionActive = TransactionSynchronizationManager.isSynchronizationActive() && TransactionSynchronizationManager.isActualTransactionActive();
        //当事务启动同步且当前事务处于活动状态，则进行注册
        if (transactionActive) {
            List<Runnable> eventList = EVENT_THREAD_LOCAL.get();
            //同一个事务的合并到一起处理
            if (CollectionUtils.isEmpty(eventList)) {
                eventList = new ArrayList<>();
                EVENT_THREAD_LOCAL.set(eventList);
                TransactionSynchronizationManager.registerSynchronization(this);
            }
            eventList.add(runnable);
        }
        return transactionActive;
    }

}
