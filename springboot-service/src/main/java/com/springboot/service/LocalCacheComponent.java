package com.springboot.service;

import com.springboot.dao.dto.UserDTO;
import com.springboot.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

/**
 * SpringBoot集成Caffeine
 */
@Slf4j
@Component
public class LocalCacheComponent {

    @Autowired
    private UserRepository userRepository;

    @CachePut(value = "user", key = "#userDTO.id")
    public UserDTO addUser(UserDTO userDTO) {
        userRepository.addUser(userDTO);
        return userDTO;
    }

    @CacheEvict(value = "user", key = "#id")
    public void remove(Long id) {
        userRepository.delete(id);
    }

    @Caching
    @Cacheable(value = "user", key = "#id")
    public UserDTO getUserById(Long id) {
        log.info("根据id[{}]获取用户", id);
        return userRepository.getById(id);
    }
}
