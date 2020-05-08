package com.springboot.controller;

import com.springboot.annotation.ZkLock;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DistributLockController {
    @Autowired
    UserService userService;



    @ZkLock(key = "zkLock")
    @PostMapping("/deleteUser")
    public boolean deleteUser(Integer userId) {
        userService.delete(userId);
        return true;
    }
}
