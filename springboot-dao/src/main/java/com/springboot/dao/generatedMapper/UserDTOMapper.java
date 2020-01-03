package com.springboot.dao.generatedMapper;

import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOExample;
import com.springboot.dao.dto.UserDTOKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface UserDTOMapper {
    long countByExample(UserDTOExample example);

    int deleteByExample(UserDTOExample example);

    int deleteByPrimaryKey(UserDTOKey key);

    int insert(UserDTO record);

    int insertSelective(UserDTO record);

    List<UserDTO> selectByExampleWithRowbounds(UserDTOExample example, RowBounds rowBounds);

    List<UserDTO> selectByExample(UserDTOExample example);

    UserDTO selectByPrimaryKey(UserDTOKey key);

    int updateByExampleSelective(@Param("record") UserDTO record, @Param("example") UserDTOExample example);

    int updateByExample(@Param("record") UserDTO record, @Param("example") UserDTOExample example);

    int updateByPrimaryKeySelective(UserDTO record);

    int updateByPrimaryKey(UserDTO record);
}