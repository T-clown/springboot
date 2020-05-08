package com.springboot.service;

import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.User;
import com.springboot.statemachine.StateMachineContext.Operator;

public interface UserService {
    /**
     * 保存或修改用户
     *
     * @param request 用户对象
     * @return 操作结果
     */
    boolean saveUser(CreateUserRequest request);

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    User get(Integer id);

    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Integer id);

     void updateStudent(UserDTO studentDTO, Operator operator);

     UserDTO getStudent(int id);
}
