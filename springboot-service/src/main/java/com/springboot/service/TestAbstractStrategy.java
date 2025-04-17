package com.springboot.service;

import com.springboot.domain.entity.UserDTO;

public abstract class TestAbstractStrategy implements Stragegy {
    protected UserService userService;

    public TestAbstractStrategy(UserService userService) {
        this.userService = userService;
    }

    public abstract UserDTO test(Long userId);

    protected UserDTO getUserById(Long userId){
        return userService.getUserById(userId);
    }
}
