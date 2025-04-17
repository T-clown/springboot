package com.springboot.test.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.domain.entity.UserDTO;
import com.springboot.mapper.UserSupport;
import com.springboot.service.UserService;
import com.springboot.test.SpringbootApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class PageTest extends SpringbootApplicationTests {
    @Autowired(required = false)
    private UserSupport userDTOMapper;

    @Autowired
    UserService userService;

    @Test
    public void transactional() {
        //userService.updateUser();
    }

    @Test
    public void pageTest() {
        int currentPage = 1;
        int pageSize = 5;
        String orderBy = "id desc";
        PageHelper.startPage(currentPage, pageSize, orderBy);
        List<UserDTO> users = userDTOMapper.listByIds(null);
        PageInfo<UserDTO> pageInfo = new PageInfo<>(users);
        Assert.assertEquals(5, pageInfo.getSize());
        log.info("【userPageInfo】= {}", pageInfo);
    }

}
