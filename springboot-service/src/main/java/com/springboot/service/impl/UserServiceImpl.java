package com.springboot.service.impl;

import com.alibaba.fastjson.JSON;

import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOKey;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Async
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    UserDTOMapper userDTOMapper;

    /**
     * 保存或修改用户
     *
     * @param request 用户对象
     * @return 操作结果
     */
    @Override
    public boolean saveUser(CreateUserRequest request) {
        log.info("添加用户,user:{}", JSON.toJSONString(request));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        userDTO.setGender(request.getGender().getName());
        userDTOMapper.insert(userDTO);
        return true;
    }

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */

    @Override
    public User get(Integer id) {
        UserDTOKey key = new UserDTOKey();
        key.setId(id);
        UserDTO userDTO = userDTOMapper.selectByPrimaryKey(key);
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }

    /**
     * 删除
     *
     * @param id key值
     */

    @Override
    public void delete(Integer id) {
        log.info("删除用户【id】= {}", id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(UpdateUserRequest request) {
        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("UserServiceImpl当前线程：" + Thread.currentThread().getName());
    }

    @Override
    public UserDTO getStudent(int id) {
        UserDTOKey key = new UserDTOKey();
        key.setId(id);
        return userDTOMapper.selectByPrimaryKey(key);
    }
}
