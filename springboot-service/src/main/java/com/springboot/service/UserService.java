package com.springboot.service;

import com.github.pagehelper.PageInfo;
import com.springboot.common.entity.PageParam;
import com.springboot.domain.entity.CreateUserRequest;
import com.springboot.domain.entity.UpdateUserRequest;
import com.springboot.domain.entity.UserDTO;
import com.springboot.domain.entity.UserQueryRequest;

import java.util.List;

public interface UserService {
    /**
     * 保存或修改用户
     *
     * @param request 用户对象
     * @return 操作结果
     */
    void addUser(CreateUserRequest request);

    void add(CreateUserRequest request);

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    UserDTO getUserById(Long id);

    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Long id);

    void update(UpdateUserRequest request);

    List<UserDTO> list(UserQueryRequest request);

    PageInfo<UserDTO> pageQuery(PageParam<UserQueryRequest> pageParam);
}
