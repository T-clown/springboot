package com.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.springboot.annotation.ImportSelector;
import com.springboot.common.constants.Constants;
import com.springboot.common.entity.Result;
import com.springboot.common.enums.CommonYN;
import com.springboot.common.util.ResultUtil;
import com.springboot.delay.DelayDTO;
import com.springboot.delay.RedisDelayKey;
import com.springboot.delay.RedisDelayQueue;
import com.springboot.entity.DelayTask;
import com.springboot.entity.Yellow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 同目录下properties比yml优先级高
 * 配置文件读取顺序：resources下的config目录文件>resources下的配置文件
 * 读取properties中文会乱码，可转成yml文件解决
 *
 *  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
 * @import注解主要作用是将类的实例加入到IOC容器中
 */
@RestController
@Slf4j
@ImportSelector(mode = CommonYN.YES)
//@Import(UserServiceImpl.class)
public class PropertiesController {
    private final AtomicInteger counter = new AtomicInteger();

    @Autowired
    private Yellow yellow;

    @Value("${p.name}")
    private String name;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private DelayQueue<DelayTask> delayQueue = new DelayQueue<>();


    //@TokenRateLimiter(qps = 0.5)
    @GetMapping(value = "/yellow")
    public Result<Yellow> properties(@RequestParam(value = "name", defaultValue = "美女") String name) {
        yellow.setName("访问8083端口");
        yellow.setId(counter.incrementAndGet());
        return ResultUtil.success(yellow);
    }

    @PostMapping("/test")
    public Result<Void> test(@RequestBody List<Long> ids){
        log.info(JSON.toJSONString(ids));
        return ResultUtil.success();
    }

    @PostMapping("/send")
    public Result<Void> send(@RequestParam("message") String message) {
        ListenableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(Constants.KAFKA_TOPIC_NAME, message);
        sendResult.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("消息发送成功:topic:{},partition:{},key:{},part{}", result.getProducerRecord().topic(), result.getProducerRecord().partition(), result.getProducerRecord().key(), result.getProducerRecord().value());
            }

            @Override
            public void onFailure(Throwable ex) {

            }
        });
        return ResultUtil.success();
    }


}
