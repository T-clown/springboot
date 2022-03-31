package com.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.springboot.annotation.LockKeyParam;
import com.springboot.annotation.RedisLock;
import com.springboot.common.TransactionalComponent;
import com.springboot.common.TransactionalUtil;
import com.springboot.common.entity.Page;
import com.springboot.common.entity.PageResult;
import com.springboot.common.exception.ServiceRuntimeException;
import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.entity.UserQueryRequest;
import com.springboot.service.CallBackService;
import com.springboot.service.UserService;
import com.springboot.service.converter.UserConverter;
import com.springboot.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionalComponent transactionalComponent;

    @Autowired
    private CallBackService callBackService;


    //@Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(CreateUserRequest request) {
        //TransactionalUtil.transactional(() -> add(request));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userRepository.addUser(userDTO);
        callBackService.execute(()-> asyncLog(userDTO.getUsername()));
        return true;
    }
    private void asyncLog(String username){
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> log(username));
        //log(username);
    }
    private void log(String userName){
        try {
            Thread.sleep(1000L);
            log.info("用户:{}已添加",userName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CreateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userRepository.addUser(userDTO);
    }

    @Override
    public User getUserById(Integer id) {
        UserDTO userDTO = userRepository.getById(id);
        return UserConverter.convert(userDTO);
    }

    @Override
    public void delete(Integer id) {
        transactionalComponent.execute(() -> userRepository.delete(id));
    }

    @RedisLock(key = "user")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(@LockKeyParam(fields = "id") UpdateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        return userRepository.update(userDTO);
    }

    @Override
    public List<User> list(UserQueryRequest request) {
        List<UserDTO> userDTOS = userRepository.list(request);
        return JSON.parseArray(JSON.toJSONString(userDTOS), User.class);
    }

    @Override
    public PageResult<User> page(UserQueryRequest request, Page page) {
        PageResult<UserDTO> pageResult = userRepository.page(request, page);
        return UserConverter.convertPageResult(pageResult,UserConverter::convert);
    }

}
