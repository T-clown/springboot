package com.springboot.common.delay;

import com.springboot.common.delay.handler.DelayDTO;
import com.springboot.common.delay.handler.DelayTaskHandler;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * source:https://github.com/pengxincheng/redisson-delayed-queue-demo
 */
@Slf4j
@Configuration
public class RedisDelayConfig {

    @Resource
    private RedisDelayQueue redisDelayQueue;

    @Autowired
    private DelayTaskHandler delayTaskHandler;

    @Qualifier("asyncTaskExecutor")
    @Autowired
    private ThreadPoolTaskExecutor executor;


    @Bean(value = "delayEngine", destroyMethod = "destroy")
    public RedisDelayEngine<DelayDTO> taskStartDelayEngine() {
        log.info("----------------延时任务引擎初始化taskStartDelayEngine----------------");
        RedisDelayEngine<DelayDTO> redisDelayEngine = new RedisDelayEngine<>(redisDelayQueue,
                RedisDelayKey.DELAY_QUEUE_KEY, t -> delayTaskHandler.handler(t), DelayDTO.class, executor);
        return redisDelayEngine;
    }

}
