package com.springboot.controller;

import com.springboot.common.Result;
import com.springboot.common.ResultUtil;
import com.springboot.constants.KafkaConsts;
import com.springboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis限流
 */
@RestController
public class RedisController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 测试发送消息
     */
    @PostMapping("/sendMessage")
    public void testSend() {
        kafkaTemplate.send(KafkaConsts.KAFKA_TOPIC_NAME, "hello,kafka...");
    }

    /**
     * https://mp.weixin.qq.com/s/2RJqnJjwrDop4DTSnjV6yA
     * @param user
     * @return
     */
    @PostMapping("/validator")
    public Result validator(@RequestBody @Validated User user) {
        return ResultUtil.success(user);
    }
}
