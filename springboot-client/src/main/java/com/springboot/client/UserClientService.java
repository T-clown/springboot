package com.springboot.client;

public interface UserClientService {

    String getUserName(Long userId);

    void addUser(String userName);
}
