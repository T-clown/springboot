package com.springboot.config;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 核心线程数（默认线程数）
     */
    private static final int corePoolSize = 10;
    /**
     * 最大线程数
     */
    private static final int maxPoolSize = 20;
    /**
     * 允许线程空闲时间（单位：默认为秒）
     */
    private static final int keepAliveTime = 10;
    /**
     * 缓冲队列大小
     */
    private static final int queueCapacity = 200;
    /**
     * 线程池名前缀
     */
    private static final String threadNamePrefix = "Async-Service-";

    @Bean("taskExecutor") // bean的名称，默认为首字母小写的方法名
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
        executor.setThreadNamePrefix(threadNamePrefix);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(queueCapacity), new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private static final String NAME_PREFIX = "ThreadPoolExecutor";

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
                //当线程出现异常后执行此方法
                t.setUncaughtExceptionHandler((t1, e) -> {
                    System.out.println(t1.getName() + "\t" + e.getMessage());
                });
                return t;
            }
        }, new AbortPolicy());

        Runtime.getRuntime().addShutdownHook(new Thread(executor::shutdown));
        return executor;
    }

}