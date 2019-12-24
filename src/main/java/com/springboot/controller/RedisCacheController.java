package com.springboot.controller;

import java.util.List;

import com.springboot.entity.User;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis缓存
 */
@RestController
public class RedisCacheController {
    @Autowired
    UserService userService;

    @PostMapping("/getUser")
    public User getUser(Long userId) {
        return userService.get(userId);
    }
}
