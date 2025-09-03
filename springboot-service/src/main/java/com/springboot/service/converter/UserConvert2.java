package com.springboot.service.converter;

import com.springboot.domain.entity.UserDTO;
import com.springboot.mapper.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author macbookpro
 */
@Mapper
public interface UserConvert2 extends BaseMapper<UserDTO, UserPO> {
    UserConvert2 INSTANCE = Mappers.getMapper(UserConvert2.class);

}
