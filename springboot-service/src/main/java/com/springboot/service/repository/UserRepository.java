package com.springboot.service.repository;

import com.github.pagehelper.PageHelper;
import com.springboot.common.entity.Page;
import com.springboot.common.entity.PageResult;
import com.springboot.common.enums.CommonYN;
import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.dto.UserDTOExample;
import com.springboot.dao.dto.UserDTOKey;
import com.springboot.dao.extendedMapper.UserMapper;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import com.springboot.entity.UserQueryRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Repository
public class UserRepository {

    @Resource
    private UserDTOMapper userDTOMapper;

    @Autowired
    private UserMapper userMapper;

    public Integer addUser(UserDTO userDTO) {
        userDTO.setIsDeleted((short) 0);
        userMapper.insert(Collections.singletonList(userDTO));
        return userDTO.getId();
    }

    public void addUser(List<UserDTO> userDTO) {
        userMapper.insert(userDTO);
    }

    public UserDTO getById(int id) {
        UserDTOKey key = new UserDTOKey();
        key.setId(id);
        return userDTOMapper.selectByPrimaryKey(key);
    }

    public void add(List<UserDTO> userDTOs) {

    }

    public void delete(Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setIsDeleted(CommonYN.YES.value());
        userDTOMapper.updateByPrimaryKeySelective(userDTO);
    }

    public List<UserDTO> list(UserQueryRequest request) {
        UserDTOExample example = new UserDTOExample();
        UserDTOExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(request.getUsername())) {
            criteria.andUsernameLike(request.getUsername() + "%");
        }
        if (StringUtils.isNotBlank(request.getPhone())) {
            criteria.andPhoneEqualTo(request.getPhone());
        }
        if (StringUtils.isNotBlank(request.getGender())) {
            criteria.andGenderEqualTo(request.getGender());
        }
        if (StringUtils.isNotBlank(request.getEmail())) {
            criteria.andEmailEqualTo(request.getEmail());
        }
        criteria.andIsDeletedEqualTo(CommonYN.NO.value());
        return userDTOMapper.selectByExample(example);
    }

    public boolean update(UserDTO userDTO) {
        //userDTO.setUpdateTime(LocalDateTime.now());
        return 1 == userDTOMapper.updateByPrimaryKeySelective(userDTO);
    }

    public PageResult<UserDTO> page(UserQueryRequest request, Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<UserDTO> list = list(request);
        PageResult<UserDTO> pageResult = new PageResult<>(list);
        PageHelper.clearPage();
        return pageResult;
    }
}
