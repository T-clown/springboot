package com.springboot.service.impl;

import com.springboot.common.TransactionalUtil;
import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import com.springboot.service.repository.UserRepository;
import com.springboot.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;


    @Override
    public boolean addUser(CreateUserRequest request) {
            TransactionalUtil.transactional(() -> add(request));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(CreateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userRepository.addUser(userDTO);
        int a=1/0;
    }

    @Override
    public User getUserById(Integer id) {
        UserDTO userDTO = userRepository.getUserDTOById(id);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    @Override
    public void delete(Integer id) {
        log.info("删除用户【id】= {}", id);
    }

    @Transactional
    @Override
    public boolean updateUser(UpdateUserRequest request) throws Exception {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        int a=1/0;
        boolean result = userRepository.update(userDTO);
        return result;
    }

}
