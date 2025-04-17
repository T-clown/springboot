package com.springboot.utils.trransaction.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author macbookpro
 */
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
