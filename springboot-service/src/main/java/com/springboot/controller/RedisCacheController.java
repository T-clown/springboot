package com.springboot.controller;

import com.springboot.common.constants.Constants;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis缓存
 */
@RestController
public class RedisCacheController {
    @Autowired
    UserService userService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 测试发送消息
     */
    @PostMapping("/sendMessage")
    public void testSend() {
        kafkaTemplate.send(Constants.KAFKA_TOPIC_NAME, "hello,kafka...");
    }

    @PostMapping("/getUser")
    public User getUser(Integer userId) {
        return userService.getUserById(userId);
    }
}
