package com.springboot.util;

import com.springboot.entity.RedisDataEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * redis延迟队列服务
 */
@Slf4j
@Component
public class RedisDelayTaskService {

    @Autowired
    private RedissonClient redissonClient;

    private  RBlockingDeque<T> blockingDeque;
    private RDelayedQueue<T> delayedQueue;

    @PostConstruct
    public void init(){

    }

    /**
     * 添加到消息队列
     * @param <T>
     */
    public <T> void addQueue(RedisDataEntity<T> entity){
        try {
            log.info("添加到延时队列 entity【{}】",entity);
            RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(entity.getQueueName());
            RDelayedQueue<T> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            delayedQueue.offerAsync(entity.getData(),entity.getDelay(),entity.getTimeUnit());
        } catch (Exception e) {
            log.error("添加到延时队列失败："+e.getMessage(),e);
            throw new RuntimeException("添加到延时队列失败");
        }
    }


    /**
     * 取值
     */
    public <T> T take(String queueName) throws InterruptedException {
        RBlockingDeque<T> blockingDeque = redissonClient.getBlockingDeque(queueName);
        return blockingDeque.take();
    }

}

