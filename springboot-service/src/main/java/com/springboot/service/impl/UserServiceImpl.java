package com.springboot.service.impl;

import com.alibaba.fastjson.JSON;

import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOExample;
import com.springboot.dao.dto.UserDTOExample.Criteria;
import com.springboot.dao.dto.UserDTOKey;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import com.springboot.statemachine.StateMachineContext.Operator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@CacheConfig(cacheNames = "caffeineCacheManager")
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
    @Cacheable(key = "#id")
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
    @CacheEvict(key = "#id")
    @Override
    public void delete(Integer id) {

        log.info("删除用户【id】= {}", id);
    }
    @CachePut(key = "#userDTO.id")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStudent(UserDTO userDTO, Operator operator) {
        UserDTOExample example = new UserDTOExample();
        Criteria criteria = example.createCriteria();
        //criteria.andIdEqualTo(11);
        //UserDTO.setUsername("赵日天");
        //UserDTO.setClassId(3);
        //UserDTO.setSex("男");
        // UserDTOMapper.updateByExampleSelective(userDTO,example);
        // int a = 2 / 0;
    }

    @Override
    public UserDTO getStudent(int id) {
        UserDTOKey key = new UserDTOKey();
        key.setId(id);
        return userDTOMapper.selectByPrimaryKey(key);
    }
}
