package com.springboot.service;

import org.springframework.context.ApplicationEvent;

public class UserTransactionEvent extends ApplicationEvent {
    private String username;

    public UserTransactionEvent(Object source, String username) {
        super(source);
        this.username = username;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }
}
