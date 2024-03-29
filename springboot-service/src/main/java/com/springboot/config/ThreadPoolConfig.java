package com.springboot.config;

import java.lang.ref.WeakReference;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 线程池配置
 */
@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 核心线程数（默认线程数）
     */
    private static final int CORE_POOL_SIZE = 5;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 10;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int KEEP_ALIVE_TIME = 0;
    /**
     * 缓冲队列大小
     */
    private static final int QUEUE_CAPACITY = 2000;
    /**
     * 线程池名前缀
     */
    private static final String THREAD_NAME_PREFIX = "Async-Service-";

    /**
     * bean的名称，默认为首字母小写的方法名
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(5);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor asyncTaskExecutor2() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(5);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler syncScheduler = new ThreadPoolTaskScheduler();
        syncScheduler.setPoolSize(5);
        // 这里给线程设置名字，主要是为了在项目能够更快速的定位错误。
        syncScheduler.setThreadGroupName("定时任务线程池");
        syncScheduler.setThreadNamePrefix("threadPoolTaskScheduler-");
        syncScheduler.initialize();
        return syncScheduler;
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(QUEUE_CAPACITY), new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private static final String NAME_PREFIX = "ThreadPoolExecutor-";

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(Thread.currentThread().getThreadGroup(), r,
                    NAME_PREFIX + threadNumber.getAndIncrement(), 0);
                //是否守护进程
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                if (t.getPriority() != Thread.NORM_PRIORITY) {
                    t.setPriority(Thread.NORM_PRIORITY);
                }
                //当线程出现异常后执行此方法，只对execute有用
                t.setUncaughtExceptionHandler((t1, e) -> {
                    log.error(t1.getName() + "\t" + e.getMessage());
                });
                return t;
            }
        }, new AbortPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
        return executor;
    }

    @Bean
    public ThreadPoolExecutor asyncExecutor() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE , KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(QUEUE_CAPACITY), new ThreadPoolExecutor.CallerRunsPolicy());
    }


}
