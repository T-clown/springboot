package com.springboot.service.converter;

import com.springboot.domain.entity.TreeNodeDTO;
import com.springboot.domain.entity.TreeNodeEntity;
import com.springboot.domain.entity.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author macbookpro
 */
@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper( UserConvert.class );

    UserDTO convert(UserDTO userDTO);

    @Mapping(source = "value", target = "value2")
    TreeNodeDTO convert(TreeNodeEntity entity);

    @Mapping(source = "value", target = "value2")
    List<TreeNodeDTO> convert(List<TreeNodeEntity> entity);
}
