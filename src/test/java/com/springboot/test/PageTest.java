//package com.springboot.test;
//
//import java.util.List;
//
//import com.github.pagehelper.PageHelper;
//import com.github.pagehelper.PageInfo;
//import com.springboot.SpringbootApplicationTests;
//import com.springboot.service.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@Slf4j
//public class PageTest extends SpringbootApplicationTests {
//    @Autowired(required = false)
//    private StudentDTOMapper studentDTOMapper;
//
//    @Autowired
//    UserService userService;
//
//    @Test
//    public void transactional() {
//        //userService.updateStudent();
//    }
//
//    @Test
//    public void pageTest() {
//        int currentPage = 1;
//        int pageSize = 5;
//        String orderBy = "id desc";
//        long count = studentDTOMapper.countByExample(null);
//        PageHelper.startPage(currentPage, pageSize, orderBy);
//        List<StudentDTO> users = studentDTOMapper.selectByExample(null);
//        PageInfo<StudentDTO> pageInfo = new PageInfo<>(users);
//        Assert.assertEquals(5, pageInfo.getSize());
//        Assert.assertEquals(count, pageInfo.getTotal());
//        log.info("【userPageInfo】= {}", pageInfo);
//    }
//
//}
