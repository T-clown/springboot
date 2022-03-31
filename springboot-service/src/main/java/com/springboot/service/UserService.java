package com.springboot.service;

import com.springboot.common.entity.Page;
import com.springboot.common.entity.PageResult;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.entity.UserQueryRequest;

import java.util.List;

public interface UserService {
    /**
     * 保存或修改用户
     *
     * @param request 用户对象
     * @return 操作结果
     */
    boolean addUser(CreateUserRequest request);

    void add(CreateUserRequest request);
    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    User getUserById(Integer id);
    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Integer id);

    boolean update(UpdateUserRequest request) throws Exception;

    List<User> list(UserQueryRequest request);


    PageResult<User> page(UserQueryRequest request, Page page);
}
