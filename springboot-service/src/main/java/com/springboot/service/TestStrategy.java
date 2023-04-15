package com.springboot.service;

import com.springboot.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TestStrategy extends TestAbstractStrategy{

    public TestStrategy(UserService userService) {
        super(userService);
    }

    @Override
    public User test(Integer userId) {
        return getUserById(userId);
    }
}
