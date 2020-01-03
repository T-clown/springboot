package com.springboot.service;

import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.User;
import com.springboot.statemachine.StateMachineContext.Operator;

public interface UserService {
    /**
     * 保存或修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    User saveOrUpdate(User user);

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    User get(Long id);

    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Long id);

     void updateStudent(UserDTO studentDTO, Operator operator);

     UserDTO getStudent(int id);
}
