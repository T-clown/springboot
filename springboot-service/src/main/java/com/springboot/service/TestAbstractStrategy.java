package com.springboot.service;

import com.springboot.entity.User;

public abstract class TestAbstractStrategy implements Stragegy {
    protected UserService userService;

    public TestAbstractStrategy(UserService userService) {
        this.userService = userService;
    }

    public abstract User test(Integer userId);

    protected User getUserById(Integer userId){
        return userService.getUserById(userId);
    }
}
