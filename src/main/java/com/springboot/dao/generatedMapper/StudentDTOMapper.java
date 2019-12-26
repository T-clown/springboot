package com.springboot.dao.generatedMapper;

import com.springboot.dao.dto.StudentDTO;
import com.springboot.dao.dto.StudentDTOExample;
import com.springboot.dao.dto.StudentDTOKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface StudentDTOMapper {
    long countByExample(StudentDTOExample example);

    int deleteByExample(StudentDTOExample example);

    int deleteByPrimaryKey(StudentDTOKey key);

    int insert(StudentDTO record);

    int insertSelective(StudentDTO record);

    List<StudentDTO> selectByExampleWithRowbounds(StudentDTOExample example, RowBounds rowBounds);

    List<StudentDTO> selectByExample(StudentDTOExample example);

    StudentDTO selectByPrimaryKey(StudentDTOKey key);

    int updateByExampleSelective(@Param("record") StudentDTO record, @Param("example") StudentDTOExample example);

    int updateByExample(@Param("record") StudentDTO record, @Param("example") StudentDTOExample example);

    int updateByPrimaryKeySelective(StudentDTO record);

    int updateByPrimaryKey(StudentDTO record);
}