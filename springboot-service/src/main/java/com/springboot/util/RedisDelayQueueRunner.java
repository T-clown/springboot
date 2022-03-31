package com.springboot.util;


import com.springboot.entity.Yellow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class RedisDelayQueueRunner implements ApplicationRunner {

    @Autowired
    private RedisDelayTaskService redisDelayTaskService;

    @Override
    public void run(ApplicationArguments args) {
        String queueName = "测试2";
        //DESC 开启新的线程(常驻)处理延迟队列
        new Thread(()->{
            while (true){
                try {
                    Yellow value = redisDelayTaskService.take(queueName);
                    if (value!=null){
                        log.info("redis延迟任务，value:{},消费时间:{}",value,LocalDateTimeUtil.format(new Date()));
                    }else{
                        log.info("redis延迟任务为空");
                    }
                } catch (InterruptedException e) {
                    log.error("延迟队列取值中断异常："+e.getMessage(),e);
                }
            }
        }).start();
    }
}

