package com.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.springboot.common.aop.annotation.LockKeyParam;
import com.springboot.common.aop.annotation.RedisLock;
import com.springboot.common.TransactionalComponent;
import com.springboot.common.entity.Page;
import com.springboot.common.entity.PageResult;
import com.springboot.dao.dto.UserDTO;
import com.springboot.domain.entity.CreateUserRequest;
import com.springboot.domain.entity.UpdateUserRequest;
import com.springboot.domain.entity.User;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.service.CallBackService;
import com.springboot.service.TransactionListener;
import com.springboot.service.UserService;
import com.springboot.service.UserTransactionEvent;
import com.springboot.service.converter.UserConverter;
import com.springboot.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

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

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addUser(CreateUserRequest request) {
        //TransactionalUtil.transactional(() -> add(request));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userRepository.addUser(userDTO);
        applicationEventPublisher.publishEvent(new UserTransactionEvent("单元评分", request.getUsername()));
//        transactionTemplate.execute(status -> {
//            Long result = 0L;
//            try {
//                result = userRepository.addUser(userDTO);
//            } catch (Exception e) {
//                status.setRollbackOnly();
//            }
//            return result;
//        });
        //callBackService.execute(()->threadPoolExecutor.execute(()->asyncLog(request.getUsername())) );
        return true;
    }

    private void asyncLog(String username) {
        //CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> log(username));
        //log(username);;
        UserQueryRequest request = new UserQueryRequest();
        request.setUsername(username);
        List<User> list = list(request);
        log.info("添加用户:{}", JSON.toJSONString(list));

    }

    private void log(String userName) {
        try {
            Thread.sleep(1000L);
            log.info("用户:{}已添加", userName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    //@Transactional(rollbackFor = Exception.class)
    public Long add(CreateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        return userRepository.addUser(userDTO);
    }

    @Override
    public User getUserById(Long id) {
        UserDTO userDTO = userRepository.getById(id);
        return UserConverter.convert(userDTO);
    }

    @Override
    public void delete(Long id) {
        transactionalComponent.execute(() -> userRepository.delete(id));
    }

    @RedisLock(key = "user")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(@LockKeyParam(fields = "id") UpdateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(request, userDTO, CopyOptions.create().ignoreNullValue());
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
        return UserConverter.convertPageResult(pageResult, UserConverter::convert);
    }

}
