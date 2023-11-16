package com.springboot.service;

import com.springboot.domain.entity.User;

public abstract class TestAbstractStrategy implements Stragegy {
    protected UserService userService;

    public TestAbstractStrategy(UserService userService) {
        this.userService = userService;
    }

    public abstract User test(Long userId);

    protected User getUserById(Long userId){
        return userService.getUserById(userId);
    }
}
