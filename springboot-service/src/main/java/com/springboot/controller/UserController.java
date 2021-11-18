package com.springboot.controller;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.alibaba.fastjson.JSON;
import com.springboot.annotation.DataSource;
import com.springboot.annotation.LockKeyParam;
import com.springboot.annotation.ZkLock;
import com.springboot.common.DynamicDataSourceContextHolder;
import com.springboot.common.DynamicRoutingDataSource;
import com.springboot.common.entity.Page;
import com.springboot.common.entity.Result;
import com.springboot.common.util.ResultUtil;
import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.DataSourceInfo;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.extend.TestFactoryBean;
import com.springboot.service.AccountService;
import com.springboot.service.UserService;
import com.springboot.service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @PostConstruct
    public void init() {
        log.info("UserController初始化。。。。。。。。。");
    }

    /**
     * https://mp.weixin.qq.com/s/2RJqnJjwrDop4DTSnjV6yA
     * https://mp.weixin.qq.com/s/uOUAmdeX88Cv0mXvBtQTnQ
     *
     * @param request
     * @return 参数为对象的：方法上加@Valid 或者 @Validated
     */
    //@ZkLock(key = "zklock")
    @PostMapping("/add")
    public Result add(@LockKeyParam(fields = {"username","phone"})@RequestBody @Valid CreateUserRequest request) {
        return ResultUtil.success(userService.addUser(request));
    }

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    DataSourceInfo dataSourceInfo;
    @Autowired
    private TestFactoryBean testFactoryBean;

    @Resource(name = "&testFactoryBean")
    private TestFactoryBean testFactoryBean2;

    @Autowired
    private UserRepository userRepository;

    /**
     * 类上@Validated+方法上@Valid
     *
     * @param id
     * @return
     */
    @DataSource(name = "slave")
    @PostMapping("get/{id}")
    protected Result<User> getUserById(@Valid @PathVariable("id") @Max(value = 5, message = "超过 id 的范围了") Integer id) {
        System.out.println("TestFactoryBean类型  {}"+testFactoryBean.getClass());
        System.out.println("TestFactoryBean2类型  {}"+testFactoryBean2.getClass());
        if (id == 3) {
            dynamicRoutingDataSource.addDataSource(dataSourceInfo.getProperties());
            DynamicDataSourceContextHolder.setDataSourceKey(dataSourceInfo.getUrl());
        }
        return ResultUtil.success(userService.getUserById(id));
    }


    /**
     * 类上@Validated+方法上@Valid
     *
     * @param name
     * @return
     */
    @PostMapping("/get")
    public Result<User> get(
            @Valid @RequestParam(value = "name",required = false) @Size(max = 6, message = "超过 username 的范围了") @NotBlank String name,
            @Valid @RequestParam(value = "id",required = false) @NotNull(message = "id不能为空") Integer id, Page page) {
        log.info("page:{}",JSON.toJSONString(page));
        User user = new User();
        user.setUsername(name);
        user.setId(id);
        return ResultUtil.success(user);
    }

    @PostMapping("/get/all")
    public Result<User> getall() {
        List<UserDTO> userDTOS = userRepository.getUserDTOS();
        System.out.println(JSON.toJSONString(userDTOS));
        return ResultUtil.success(null);
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid UpdateUserRequest request) throws Exception {
        return ResultUtil.success(userService.updateUser(request));
    }

}
