package dao.generatedMapper;

import java.util.List;

import dao.dto.MemberAccountDTO;
import dao.dto.MemberAccountDTOExample;
import dao.dto.MemberAccountDTOKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface MemberAccountDTOMapper {
    long countByExample(MemberAccountDTOExample example);

    int deleteByExample(MemberAccountDTOExample example);

    int deleteByPrimaryKey(MemberAccountDTOKey key);

    int insert(MemberAccountDTO record);

    int insertSelective(MemberAccountDTO record);

    List<MemberAccountDTO> selectByExampleWithRowbounds(MemberAccountDTOExample example, RowBounds rowBounds);

    List<MemberAccountDTO> selectByExample(MemberAccountDTOExample example);

    MemberAccountDTO selectByPrimaryKey(MemberAccountDTOKey key);

    int updateByExampleSelective(@Param("record") MemberAccountDTO record, @Param("example") MemberAccountDTOExample example);

    int updateByExample(@Param("record") MemberAccountDTO record, @Param("example") MemberAccountDTOExample example);

    int updateByPrimaryKeySelective(MemberAccountDTO record);

    int updateByPrimaryKey(MemberAccountDTO record);
}