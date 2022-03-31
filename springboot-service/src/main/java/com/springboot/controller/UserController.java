package com.springboot.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import com.springboot.annotation.DataSource;
import com.springboot.annotation.LockKeyParam;
import com.springboot.common.DynamicRoutingDataSource;
import com.springboot.common.HystrixComponent;
import com.springboot.common.entity.Page;
import com.springboot.common.entity.PageResult;
import com.springboot.common.entity.Result;
import com.springboot.common.util.ResultUtil;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.DataSourceInfo;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.entity.UserQueryRequest;
import com.springboot.extend.TestFactoryBean;
import com.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

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
    public Result add(@LockKeyParam(fields = {"username", "phone"}) @RequestBody @Valid CreateUserRequest request) {
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

    /**
     * 类上@Validated+方法上@Valid
     *
     * @param id
     * @return
     */
    @DataSource(name = "master")
    @PostMapping("get/{id}")
    protected Result<User> getUserById(@Valid @PathVariable("id") @Min(value = 0, message = "id最小为1") Integer id) {
        log.info("TestFactoryBean类型  {}", testFactoryBean.getClass());
        log.info("TestFactoryBean2类型  {}", testFactoryBean2.getClass());
        User userById = userService.getUserById(id);
        return ResultUtil.success(userById);
    }


    /**
     * 类上@Validated+方法上@Valid
     *
     * @return
     */
    @PostMapping("/page")
    public Result<PageResult<User>> page(@RequestBody UserQueryRequest request, Page page) {
        log.info("page:{}", JSON.toJSONString(page));
        PageResult<User> pageResult = userService.page(request, page);
        return ResultUtil.success(pageResult);
    }
    @Autowired
    private HystrixComponent hystrixComponent;

    @PostMapping("/list")
    public Result<List<User>> list(@RequestBody UserQueryRequest request) {
        List<User> users = hystrixComponent.getUsers();
        return ResultUtil.success(users);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return ResultUtil.success(true);
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid UpdateUserRequest request) throws Exception {
        return ResultUtil.success(userService.update(request));
    }

    public static void main(String[] args) {
        User source=new User();
        User target=new User();
        target.setUsername("aaa");
        BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true).ignoreNullValue());
        System.out.println(JSON.toJSONString(target));
    }

}
