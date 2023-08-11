package com.springboot.client;

import com.springboot.domain.entity.CreateUserRequest;
import com.springboot.domain.entity.User;
import com.springboot.service.UserService;
import io.seata.core.context.RootContext;
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
        User userById = userService.getUserById(userId);
        return userById == null ? "没有此用户" : userById.getUsername();
    }

    @Override
    public Long addUser(String userName) {
        log.info("全局事务ID:[{}]" , RootContext.getXID());
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername(userName);
        request.setPhone("123456");
        return userService.add(request);
    }
}
