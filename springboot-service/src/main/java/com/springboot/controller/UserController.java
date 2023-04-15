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
import com.springboot.service.TestStrategy;
import com.springboot.service.UserService;
import com.springboot.util.LocalDateTimeUtil;
import com.springboot.util.StopWatchUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;
@Api(tags = {"用户管理"})
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    DataSourceInfo dataSourceInfo;

    @Autowired
    private TestFactoryBean testFactoryBean;

    @Resource(name = "&testFactoryBean")
    private TestFactoryBean testFactoryBean2;

    @Autowired
    private TestStrategy testStrategy;

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
    @ApiOperation("新增")
    //@ZkLock(key = "zklock")
    @PostMapping("/add")
    public Result<Boolean> add(@LockKeyParam(fields = {"username", "phone"}) @RequestBody @Valid CreateUserRequest request) {
        return ResultUtil.success(userService.addUser(request));
    }

    /**
     * 类上@Validated+方法上@Valid
     *
     * @param id
     * @return
     */
    @ApiOperation("获取用户详情")
    @DataSource(name = "master")
    @PostMapping("get/{id}")
    protected Result<User> getUserById(@Valid @PathVariable("id") @Min(value = 0, message = "id最小为1") Integer id) {
        log.info("TestFactoryBean类型  {}", testFactoryBean.getClass());
        log.info("TestFactoryBean2类型  {}", testFactoryBean2.getClass());
        //User userById = userService.getUserById(id);
        User userById = testStrategy.test(id);
        log.info("获取用户,id:{},result:{}", id, JSON.toJSONString(userById));
        return ResultUtil.success(userById);
    }


    /**
     * 类上@Validated+方法上@Valid
     *
     * @return
     */
    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result<PageResult<User>> page(@RequestBody UserQueryRequest request, Page page) {
        StopWatchUtil.start("测试", "获取用户列表");
        log.info("page:{}", JSON.toJSONString(page));
        PageResult<User> pageResult = userService.page(request, page);
        log.info(StopWatchUtil.prettyPrint());
        return ResultUtil.success(pageResult);
    }

    @Autowired
    private HystrixComponent hystrixComponent;

    @ApiOperation("列表查询")
    @PostMapping("/list")
    public Result<List<User>> list(@RequestBody UserQueryRequest request) {
        //List<User> users = hystrixComponent.getUsers();
        List<User> users = userService.list(request);
        log.info("查询用户列表:{}", JSON.toJSONString(users));
        return ResultUtil.success(users);
    }

    @ApiOperation("删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") Integer id) {
        userService.delete(id);
        return ResultUtil.success(true);
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid UpdateUserRequest request) throws Exception {
        return ResultUtil.success(userService.update(request));
    }

}
