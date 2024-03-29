package com.springboot.common.delay;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * redis 延时处理引擎
 *
 * @author kiprince
 */
@Slf4j
public class RedisDelayEngine<T extends BaseTask> {

    /**
     * redis延时队列处理器
     */
    private RedisDelayQueue redisDelayQueue;

    /**
     * 队列名称
     **/
    private String queueName;

    /**
     * 回调服务
     */
    //private DelayTaskListener<T> taskListener;
    private Consumer<T> handler;

    /**
     * 数据类型
     */
    private Class<T> classType;

    /**
     * 失败重试次数 默认0
     */
    private Integer tryTimes = 0;


    /**
     * 失败重试时间间隔  默认1秒
     */
    private Long reTryDelay = 1000L;
    /**
     * 线程池
     */
    private ThreadPoolTaskExecutor executor;

    private boolean executeAsync = false;

    /**
     * 设置失败重试次数，大于0时生效
     *
     * @param tryTimes 重试次数
     */
    public void setTryTimes(int tryTimes) {
        if (tryTimes > 0) {
            this.tryTimes = tryTimes;
        }
    }

    public void setReTryDelay(Long reTryDelay) {
        if (reTryDelay > 0) {
            this.reTryDelay = reTryDelay;
        }
    }

    public RedisDelayEngine(RedisDelayQueue redisDelayQueue, String queueName,
                            Consumer<T> handler, Class<T> classType, ThreadPoolTaskExecutor executor) {
        this.redisDelayQueue = redisDelayQueue;
        this.queueName = queueName;
        this.handler = handler;
        this.classType = classType;
        log.info("RedisDelayEngine线程池配置,corePoolSize:{},maxPoolSize:{},namePrefix:{}", executor.getPoolSize(), executor.getMaxPoolSize(), executor.getThreadNamePrefix());
        this.executor = executor;
        this.executeAsync = true;
        // 阻塞队列和延时队列初始化
        redisDelayQueue.getDelayQueue(queueName);
        //启动延时线程
        new DelayThread().start();
    }

    public RedisDelayEngine(RedisDelayQueue redisDelayQueue, String queueName,
                            Consumer<T> handler, Class<T> classType) {
        this.redisDelayQueue = redisDelayQueue;
        this.queueName = queueName;
        this.handler = handler;
        this.classType = classType;
        // 阻塞队列和延时队列初始化
        redisDelayQueue.getDelayQueue(queueName);
        //启动延时线程
        new DelayThread().start();
    }

    private void errorHandler(T t) {
        try {
            String taskId = t.taskIdentity();
            int errorTimes = t.getErrorTimes();
            if (errorTimes <= tryTimes) {
                log.info("任务重试, taskId:{}, errorTimes:{}", taskId, errorTimes);
                //任务延迟一秒重试,失败次数+1
                t.setErrorTimes(errorTimes + 1);
                redisDelayQueue.addQueue(queueName, t, reTryDelay, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.error("任务重试失败", e);
        }
    }

    public class DelayThread extends Thread {

        @Override
        public void run() {
            try {
                RBlockingQueue<String> blockingFairQueue = redisDelayQueue.getBlockingQueue(queueName);
                String data = null;
                while (true) {
                    try {
                        log.info("延时处理获取队列queueName:{}", queueName);
                        data = blockingFairQueue.take();
                    } catch (InterruptedException e) {
                        log.error("延时处理获取队列元素异常，队列:{}", queueName, e);
                        Thread.currentThread().interrupt();
                    }

                    if (StringUtils.isNotEmpty(data)) {
                        // T t = JSON.parseObject(data, classType);
                        T t = JSONUtil.toBean(data, classType);
                        if (t == null) {
                            log.error("延时队列元素json解析异常:{}", data);
                            continue;
                        }
                        try {
                            log.info("触发队列:{},任务:{}", queueName, t.taskIdentity());
                            if (executeAsync) {
                                executor.execute(() -> handler.accept(t));
                            } else {
                                handler.accept(t);
                            }
                        } catch (Exception exception) {
                            log.error("延时处理元素{}:{}异常", queueName, t, exception);
                            errorHandler(t);
                        }
                    }
                }
            } catch (Throwable throwable) {
                log.error("延时处理线程错误", throwable);
            }
        }
    }

    public void destroy() {
        log.info("销毁延时队列,queueName:{}", queueName);
        redisDelayQueue.destroyQueue(queueName);
    }
}
