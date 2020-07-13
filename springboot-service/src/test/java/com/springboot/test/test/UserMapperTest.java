package com.springboot.test.test;

import java.util.Collections;
import java.util.Date;

import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.extendedMapper.UserMapper;
import com.springboot.enums.CommonYN;
import com.springboot.test.SpringbootApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class UserMapperTest extends SpringbootApplicationTests {
    @Autowired(required = false)
    UserMapper userMapper;

    @Rollback(value = false)
    @Test
    public void insert() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("clown");
        userDTO.setBirthday(new Date());
        userDTO.setEmail("414162330@qq.com");
        userDTO.setPhone("13730638402");
        userDTO.setIsDeleted(CommonYN.NO.value());
        userDTO.setGender("男");
        userDTO.setCreateTime(new Date());
        userDTO.setUpdateTime(new Date());
        userMapper.insert(Collections.singletonList(userDTO));
    }

}
