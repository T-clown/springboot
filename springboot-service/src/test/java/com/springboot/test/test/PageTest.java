package com.springboot.test.test;

import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.dao.dto.UserDTO;
import com.springboot.dao.generatedMapper.UserDTOMapper;
import com.springboot.service.UserService;
import com.springboot.test.SpringbootApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class PageTest extends SpringbootApplicationTests {
    @Autowired(required = false)
    private UserDTOMapper userDTOMapper;

    @Autowired
    UserService userService;

    @Test
    public void transactional() {
        //userService.updateStudent();
    }

    @Test
    public void pageTest() {
        int currentPage = 1;
        int pageSize = 5;
        String orderBy = "id desc";
        long count = userDTOMapper.countByExample(null);
        PageHelper.startPage(currentPage, pageSize, orderBy);
        List<UserDTO> users = userDTOMapper.selectByExample(null);
        PageInfo<UserDTO> pageInfo = new PageInfo<>(users);
        Assert.assertEquals(5, pageInfo.getSize());
        Assert.assertEquals(count, pageInfo.getTotal());
        log.info("【userPageInfo】= {}", pageInfo);
    }

}
