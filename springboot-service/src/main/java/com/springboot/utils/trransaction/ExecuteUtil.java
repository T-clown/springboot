package com.springboot.utils.trransaction;

import com.springboot.handler.CallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static com.springboot.config.ThreadPoolConfig.THREAD_POOL_EXECUTOR;

/**
 * 事务提交后执行
 *
 * @author macbookpro
 */
@Slf4j
@Component
public class ExecuteUtil implements ApplicationContextAware {
    private static ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 异步执行
     *
     * @param runnable
     */
    public static void executeAsync(Runnable runnable) {
        threadPoolTaskExecutor.execute(runnable);
    }

    public static void executeAfterTransaction(Runnable runnable) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("事务提交后执行回调");
                    // 事务提交后执行回调，如果回调的时候需要事务，则回调事务用PROPAGATION_REQUIRES_NEW
                    runnable.run();
                }
            });
        } else {
            // 无事务执行回调
            log.info("无事务执行回调");
            runnable.run();
        }
    }


    public static void executeAsyncAfterTransaction(Runnable runnable) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("事务提交后执行回调");
                    // 事务提交后执行回调，如果回调的时候需要事务，则回调事务用PROPAGATION_REQUIRES_NEW
                    threadPoolTaskExecutor.execute(runnable);
                }
            });
        } else {
            // 无事务执行回调
            log.info("无事务执行回调");
            threadPoolTaskExecutor.execute(runnable);
        }
    }

    public void execute(CallBack action) {
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
                    log.info("beforeCommit：事务提交前执行，事务即便回魂也会执行，readOnly:{}", readOnly);
                }

                @Override
                public void beforeCompletion() {
                    //这里发生异常不会传播给调用者
                    log.info("beforeCompletion：事务完成前（提交或者回滚）执行，一般用于关闭资源");
                }

                @Override
                public void afterCompletion(int status) {
                    //这里发生异常不会传播给调用者
                    log.info("afterCompletion：事务完成后（提交或者回滚）执行，一般用于关闭资源，status:{}", status);
                }
            });
        } else {
            // 无事务执行回调
            log.info("无事务执行回调");
            action.callback();
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        threadPoolTaskExecutor = applicationContext.getBean(THREAD_POOL_EXECUTOR, ThreadPoolTaskExecutor.class);
    }
}