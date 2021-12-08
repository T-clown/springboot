package com.springboot.service.repository;

import com.springboot.common.enums.CommonYN;
import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOExample;
import com.springboot.dao.dto.UserDTOKey;
import com.springboot.dao.extendedMapper.UserMapper;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import com.springboot.entity.UserQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository {

    @Resource
    private UserDTOMapper userDTOMapper;

    @Autowired(required = false)
    private UserMapper userMapper;

    public void addUser(UserDTO userDTO) {
        userMapper.insert(Collections.singletonList(userDTO));
    }

    public UserDTO getUserDTOById(int id) {
        UserDTOKey key = new UserDTOKey();
        key.setId(id);
        return userDTOMapper.selectByPrimaryKey(key);
    }

    public void addUser(List<UserDTO> userDTOs) {

    }

    public List<UserDTO> getUserDTOS(UserQueryRequest request) {
        UserDTOExample example= new UserDTOExample();
        example.createCriteria().andIsDeletedEqualTo(CommonYN.NO.value());
        return userDTOMapper.selectByExample(example);
    }

    public List<UserDTO> getUserDTOS(List<String> names) {
        return userMapper.list(names);
    }

    public boolean update(UserDTO userDTO) {
        //userDTO.setUpdateTime(LocalDateTime.now());
        return 1 == userDTOMapper.updateByPrimaryKeySelective(userDTO);
    }
}
