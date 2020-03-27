package com.springboot.service.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOExample;
import com.springboot.dao.dto.UserDTOExample.Criteria;
import com.springboot.dao.dto.UserDTOKey;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import com.springboot.statemachine.StateMachineContext.Operator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    /**
     * 模拟数据库
     */
    private static final Map<Long, User> DATABASES = Maps.newConcurrentMap();

    @Autowired(required = false)
    UserDTOMapper userDTOMapper;

    /**
     * 保存或修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    @CachePut(value = "user", key = "#user.id")
    @Override
    public User saveOrUpdate(User user) {
        DATABASES.put(user.getId(), user);
        log.info("保存用户【user】= {}", user);
        return user;
    }

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    @Cacheable(value = "user", key = "#id")
    @Override
    public User get(Long id) {
        // 我们假设从数据库读取
        log.info("查询用户【id】= {}", id);
        return DATABASES.get(id);
    }

    /**
     * 删除
     *
     * @param id key值
     */
    @CacheEvict(value = "user", key = "#id")
    @Override
    public void delete(Long id) {
        DATABASES.remove(id);
        log.info("删除用户【id】= {}", id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStudent(UserDTO userDTO, Operator operator){
        UserDTOExample example=new UserDTOExample();
        Criteria criteria= example.createCriteria();
        //criteria.andIdEqualTo(11);
        //UserDTO.setUsername("赵日天");
        //UserDTO.setClassId(3);
        //UserDTO.setSex("男");
       // UserDTOMapper.updateByExampleSelective(userDTO,example);
       // int a = 2 / 0;
    }

    @Override
    public UserDTO getStudent(int id) {
        UserDTOKey key=new UserDTOKey();
        key.setId(id);
        return userDTOMapper.selectByPrimaryKey(key);
    }
}
