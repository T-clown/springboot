package com.springboot.dao.extendedMapper;

import java.util.List;

import com.springboot.dao.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface UserMapper {
    void insert(@Param("userDTOS") List<UserDTO> userDTOS);

    List<UserDTO> list(@Param("names") List<String> names);
}
