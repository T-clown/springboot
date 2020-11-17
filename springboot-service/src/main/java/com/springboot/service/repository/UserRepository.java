package com.springboot.service.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.springboot.common.enums.CommonYN;
import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOExample;
import com.springboot.dao.dto.UserDTOKey;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired(required = false)
    UserDTOMapper userDTOMapper;

    public void addUser(UserDTO userDTO) {
        //userDTO.setCreateTime(LocalDateTime.now());
        //userDTO.setUpdateTime(LocalDateTime.now());
        userDTO.setIsDeleted(CommonYN.NO.value());
        userDTOMapper.insert(userDTO);
    }

    public UserDTO getUserDTOById(int id) {
        UserDTOKey key = new UserDTOKey();
        key.setId(id);
        return userDTOMapper.selectByPrimaryKey(key);
    }

    public void addUser(List<UserDTO> userDTOs) {

    }

    public List<UserDTO> getUserDTOs() {
        return userDTOMapper.selectByExample(new UserDTOExample());
    }

    public boolean update(UserDTO userDTO) {
        //userDTO.setUpdateTime(LocalDateTime.now());
        return 1 == userDTOMapper.updateByPrimaryKeySelective(userDTO);
    }
}
