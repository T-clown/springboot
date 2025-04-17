package com.springboot.utils.trransaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.springboot.config.ThreadPoolConfig.THREAD_POOL_EXECUTOR;

/**
 * @author macbookpro
 */
@Slf4j
@Component
public class ExecuteUtilV2 implements TransactionSynchronization, ApplicationContextAware {
    private static ThreadPoolTaskExecutor THREAD_POOL_TASK_EXECUTOR;

    /**
     * 用于存放同个事务中多个执行事件
     */
    private final ThreadLocal<List<Runnable>> EVENT_THREAD_LOCAL = new ThreadLocal<>();
    /**
     * 异步事件
     */
    private final ThreadLocal<List<Runnable>> ASYNC_EVENT_THREAD_LOCAL = new ThreadLocal<>();

    private ThreadLocal<List<Runnable>> getEventContainer(boolean async) {
        return async ? ASYNC_EVENT_THREAD_LOCAL : EVENT_THREAD_LOCAL;
    }

    private static ExecuteUtilV2 INSTANCE;

    /**
     * 同步执行
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        execute(runnable, false);
    }

    /**
     * 异步执行
     *
     * @param runnable
     */
    public static void executeAsync(Runnable runnable) {
        execute(runnable, true);
    }

    public static void execute(Runnable runnable, boolean async) {
        boolean isTransactionActive = registerEvent(runnable, async);
        //如果处于非事务状态，则直接运行
        if (!isTransactionActive) {
            log.warn("No transaction is active");
            if (async) {
                THREAD_POOL_TASK_EXECUTOR.execute(runnable);
            } else {
                runnable.run();
            }
        }
    }

    @Override
    public void afterCommit() {
        processEvent();
    }

    @Override
    public void afterCompletion(int status) {
        log.info("事务结束后移除事件");
        EVENT_THREAD_LOCAL.remove();
        ASYNC_EVENT_THREAD_LOCAL.remove();
    }

    /**
     * 事件处理
     */
    private void processEvent() {
        log.info("processEvent after transaction commit");
        List<Runnable> asyncEventList = ASYNC_EVENT_THREAD_LOCAL.get();
        //执行异步事件
        for (Runnable runnable : asyncEventList) {
            THREAD_POOL_TASK_EXECUTOR.execute(runnable);
        }
        List<Runnable> eventList = EVENT_THREAD_LOCAL.get();
        //执行同步事件
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
    private static boolean registerEvent(Runnable runnable, boolean async) {
        boolean transactionActive = TransactionSynchronizationManager.isSynchronizationActive() && TransactionSynchronizationManager.isActualTransactionActive();
        //当事务启动同步且当前事务处于活动状态，则进行注册
        if (transactionActive) {
            ThreadLocal<List<Runnable>> eventContainer = INSTANCE.getEventContainer(async);
            List<Runnable> eventList = eventContainer.get();
            //同一个事务的合并到一起处理
            if (CollectionUtils.isEmpty(eventList)) {
                eventList = new ArrayList<>();
                eventContainer.set(eventList);
                TransactionSynchronizationManager.registerSynchronization(INSTANCE);
            }
            eventList.add(runnable);
        }
        return transactionActive;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        THREAD_POOL_TASK_EXECUTOR = applicationContext.getBean(THREAD_POOL_EXECUTOR, ThreadPoolTaskExecutor.class);
        INSTANCE = applicationContext.getBean(ExecuteUtilV2.class);
    }
}
