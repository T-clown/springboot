package com.springboot.service.impl;

import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import com.springboot.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(CreateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userRepository.addUser(userDTO);
        return true;
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
    public boolean updateUser(UpdateUserRequest request) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        boolean result = userRepository.update(userDTO);
        int a = 1 / 0;
        return result;
    }

}
