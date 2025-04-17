package com.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author macbookpro
 */
@Mapper
public interface UserMapper extends CommonMapper<UserPO> {
    void insertBatch2(@Param("list") List<UserPO> list);

    List<UserPO> list(@Param("names") List<String> names);
}
