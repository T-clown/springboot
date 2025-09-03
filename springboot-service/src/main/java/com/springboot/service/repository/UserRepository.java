package com.springboot.service.repository;

import com.github.pagehelper.PageInfo;
import com.springboot.common.entity.PageParam;
import com.springboot.domain.entity.UserDTO;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.mapper.UserSupport;
import com.springboot.service.converter.UserConvert2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.springboot.config.ThreadPoolConfig.THREAD_POOL_EXECUTOR;

/**
 * @author macbookpro
 */
@Slf4j
@Repository
public class UserRepository {

    @Autowired
    private UserSupport userSupport;

    public void addUser(UserDTO userDTO) {
        userSupport.insert(userDTO);
    }

    public void addUser(List<UserDTO> userDTO) {

    }

    public UserDTO getById(Long id) {
        return userSupport.getById(id);
    }

    public void add(List<UserDTO> userDTOs) {

    }

    public void delete(Long id) {
        userSupport.delete(id);
    }

    public List<UserDTO> list(UserQueryRequest request) {
        return UserConvert2.INSTANCE.targetToSource(userSupport.list());
    }

    public void update(UserDTO userDTO) {
        userSupport.updateById(userDTO);
    }

    public PageInfo<UserDTO> pageQuery(PageParam<UserQueryRequest> pageParam) {
        return userSupport.pageQuery(pageParam);
    }

    @Async(THREAD_POOL_EXECUTOR)
    public void execute(String username) {
        log.info("新增用户:{}", username);
    }
}
