package com.springboot.service.impl;

import java.util.Map;

import com.google.common.collect.Maps;
import com.springboot.dao.dto.StudentDTO;
import com.springboot.dao.dto.StudentDTOExample;
import com.springboot.dao.dto.StudentDTOExample.Criteria;
import com.springboot.dao.dto.StudentDTOKey;
import com.springboot.dao.generatedMapper.StudentDTOMapper;
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
    StudentDTOMapper studentDTOMapper;

    /**
     * 初始化数据
     */
    static {

    }

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
    public void updateStudent(StudentDTO studentDTO, Operator operator){
        StudentDTOExample example=new StudentDTOExample();
        Criteria criteria= example.createCriteria();
        criteria.andIdEqualTo(11);
        studentDTO.setName("赵日天");
        studentDTO.setClassId(3);
        studentDTO.setSex("男");
        studentDTOMapper.updateByExampleSelective(studentDTO,example);
       // int a = 2 / 0;
    }

    @Override
    public StudentDTO getStudent(int id) {
        StudentDTOKey key=new StudentDTOKey();
        key.setId(id);
        return studentDTOMapper.selectByPrimaryKey(key);
    }
}
