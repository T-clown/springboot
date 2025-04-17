package com.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.springboot.common.TransactionalComponent;
import com.springboot.common.aop.annotation.LockKeyParam;
import com.springboot.common.aop.annotation.RedisLock;
import com.springboot.common.entity.Page;
import com.springboot.domain.entity.CreateUserRequest;
import com.springboot.domain.entity.UpdateUserRequest;
import com.springboot.domain.entity.UserDTO;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.service.UserService;
import com.springboot.utils.trransaction.event.UserTransactionEvent;
import com.springboot.service.repository.UserRepository;
import com.springboot.utils.trransaction.ExecuteUtil;
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
import java.util.concurrent.TimeUnit;

/**
 * @author macbookpro
 */
@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionalComponent transactionalComponent;

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private TransactionTemplate transactionTemplate;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addUser(CreateUserRequest request) {
        //TransactionalUtil.transactional(() -> add(request));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userRepository.addUser(userDTO);
        ExecuteUtil.executeAfterTransaction(() -> userRepository.execute(userDTO.getUsername()));
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        applicationEventPublisher.publishEvent(new UserTransactionEvent("创建用户", request.getUsername()));
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
    }

    private void asyncLog(String username) {
        //CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> log(username));
        //log(username);;
        UserQueryRequest request = new UserQueryRequest();
        request.setUsername(username);
        List<UserDTO> list = list(request);
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
    public void add(CreateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userRepository.addUser(userDTO);
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public void delete(Long id) {
        transactionalComponent.execute(() -> userRepository.delete(id));
    }

    @RedisLock(key = "user")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(@LockKeyParam(fields = "id") UpdateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(request, userDTO, CopyOptions.create().ignoreNullValue());
        userRepository.update(userDTO);
    }

    @Override
    public List<UserDTO> list(UserQueryRequest request) {
        List<UserDTO> userDTOS = userRepository.list(request);
        return JSON.parseArray(JSON.toJSONString(userDTOS), UserDTO.class);
    }

    @Override
    public PageInfo<UserDTO> pageQuery(UserQueryRequest request, Page page) {
        return userRepository.pageQuery(request);
    }

}
