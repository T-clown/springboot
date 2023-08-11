package com.springboot.service;

import com.springboot.domain.entity.User;
import org.springframework.stereotype.Service;

@Service
public class TestStrategy extends TestAbstractStrategy{

    public TestStrategy(UserService userService) {
        super(userService);
    }

    @Override
    public User test(Long userId) {
        return getUserById(userId);
    }
}
