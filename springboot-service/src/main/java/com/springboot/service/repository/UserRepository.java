package com.springboot.service.repository;

import java.util.Date;
import java.util.List;

import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOExample;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired(required = false)
    UserDTOMapper userDTOMapper;

    public void addUser(UserDTO userDTO) {
        userDTO.setCreateTime(new Date());
        userDTO.setUpdateTime(new Date());
        userDTOMapper.insert(userDTO);
    }

    public void addUser(List<UserDTO> userDTOs) {

    }

    public List<UserDTO> getUserDTOs() {
        return userDTOMapper.selectByExample(new UserDTOExample());
    }

}
