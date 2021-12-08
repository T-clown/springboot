package com.springboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.springboot.common.TransactionalUtil;
import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.entity.UserQueryRequest;
import com.springboot.service.UserService;
import com.springboot.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(UpdateUserRequest request) throws Exception {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        return userRepository.update(userDTO);
    }

    @Override
    public List<User> list(UserQueryRequest request) {
        List<UserDTO> userDTOS = userRepository.getUserDTOS(request);
        return JSON.parseArray(JSON.toJSONString(userDTOS),User.class);
    }

    @Override
    public List<User> listByNames(List<String> names) {
        List<UserDTO> userDTOS = userRepository.getUserDTOS(names);
        return JSON.parseArray(JSON.toJSONString(userDTOS),User.class);
    }

}
