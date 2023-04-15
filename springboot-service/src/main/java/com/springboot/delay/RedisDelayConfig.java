package com.springboot.delay;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Configuration
public class RedisDelayConfig {

    @Resource
    private RedisDelayQueue redisDelayQueue;

    @Autowired
    private DelayTaskHandler delayTaskHandler;

    @Autowired
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Bean(value = "delayEngine", destroyMethod = "destroy")
    public RedisDelayEngine<DelayDTO> taskStartDelayEngine() {
        log.info("----------------延时任务引擎初始化----------------");
        RedisDelayEngine<DelayDTO> redisDelayEngine = new RedisDelayEngine<>(redisDelayQueue,
                RedisDelayKey.getDelayQueueKey(), t -> delayTaskHandler.handler(t), DelayDTO.class,asyncTaskExecutor);
        redisDelayEngine.setTryTimes(3);
        redisDelayEngine.setReTryDelay(5000L);
        return redisDelayEngine;
    }

}
