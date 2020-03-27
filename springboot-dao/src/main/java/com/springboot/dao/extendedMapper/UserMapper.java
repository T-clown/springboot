package com.springboot.dao.extendedMapper;

import java.util.List;

import com.springboot.dao.dto.UserDTO;

public interface UserMapper {
    void insert(List<UserDTO> userDTOS);
}
