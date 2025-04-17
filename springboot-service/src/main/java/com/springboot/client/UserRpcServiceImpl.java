package com.springboot.client;

import com.springboot.domain.entity.CreateUserRequest;
import com.springboot.domain.entity.UserDTO;
import com.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 提供RPC接口实现
 */
@Slf4j
@DubboService
public class UserRpcServiceImpl implements UserClientService {

    @Autowired
    private UserService userService;


    @Override
    public String getUserName(Long userId) {
        //NettyCodecAdapter
        UserDTO userById = userService.getUserById(userId);
        return userById == null ? "没有此用户" : userById.getUsername();
    }

    @Override
    public void addUser(String userName) {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(userName);
        request.setPhone("123456");
        userService.add(request);
    }
}
