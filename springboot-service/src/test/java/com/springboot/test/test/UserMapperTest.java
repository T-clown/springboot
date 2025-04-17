package com.springboot.test.test;

import com.springboot.controller.PropertiesController;
import com.springboot.domain.entity.UserDTO;
import com.springboot.mapper.UserSupport;
import com.springboot.test.SpringbootApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;

public class UserMapperTest extends SpringbootApplicationTests {
    @Autowired(required = false)
    UserSupport userMapper;
    @Autowired
    PropertiesController propertiesController;

    @Rollback(value = false)
    @Test
    public void insert() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("clasdasdasdasdasown");
        userDTO.setBirthday(LocalDateTime.now());
        userDTO.setEmail("414162330@qq.com");
        userDTO.setPhone("13730638402");
        userDTO.setGender("男");
        userDTO.setCreateTime(LocalDateTime.now());
        userDTO.setUpdateTime(LocalDateTime.now());
        userMapper.insert(userDTO);
    }

    @Test
    public void properties(){
        //propertiesController.properties("");
    }

}
