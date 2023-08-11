package com.springboot.client;

public interface UserClientService {

    String getUserName(Long userId);

    Long addUser(String userName);
}
