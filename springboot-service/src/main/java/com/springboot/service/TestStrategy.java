package com.springboot.service;

import com.springboot.domain.entity.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class TestStrategy extends TestAbstractStrategy{

    public TestStrategy(UserService userService) {
        super(userService);
    }

    @Override
    public UserDTO test(Long userId) {
        return getUserById(userId);
    }
}
