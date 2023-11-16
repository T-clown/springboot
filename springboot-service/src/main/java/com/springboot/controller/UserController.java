package com.springboot.controller;

import cn.hippo4j.common.web.exception.ServiceException;
import com.alibaba.fastjson.JSON;
import com.springboot.common.aop.annotation.DataSource;
import com.springboot.common.aop.annotation.LockKeyParam;
import com.springboot.common.HystrixComponent;
import com.springboot.common.entity.Page;
import com.springboot.common.entity.PageResult;
import com.springboot.common.entity.Result;
import com.springboot.common.utils.ResultUtil;
import com.springboot.domain.entity.CreateUserRequest;
import com.springboot.domain.entity.DataSourceInfo;
import com.springboot.domain.entity.UpdateUserRequest;
import com.springboot.domain.entity.User;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.common.extension.TestFactoryBean;
import com.springboot.service.TestStrategy;
import com.springboot.service.UserService;
import com.springboot.utils.StopWatchUtil;
import com.springboot.utils.ValidationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Tag(name = "用户管理")
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    DataSourceInfo dataSourceInfo;

    @Autowired
    private TestFactoryBean testFactoryBean;

    @Resource(name = "&testFactoryBean")
    private TestFactoryBean testFactoryBean2;

    @Autowired
    private TestStrategy testStrategy;

    @Autowired
    private RedissonClient redissonSingle;

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
    @Operation(summary = "新增")
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
    @Operation(summary = "获取用户详情")
    @DataSource(name = "master")
    @PostMapping("get/{id}")
    protected Result<User> getUserById(@Valid @PathVariable("id") @Min(value = 0, message = "id最小为1") Long id) {
//        log.info("TestFactoryBean类型  {}", testFactoryBean.getClass());
//        log.info("TestFactoryBean2类型  {}", testFactoryBean2.getClass());
        //User userById = userService.getUserById(id);
//        User userById = testStrategy.test(id);
//        log.info("获取用户,id:{},result:{}", id, JSON.toJSONString(userById));
        User userById = getUser("用户");
        return ResultUtil.success(userById);
    }

    public User getUser(String name) {
        RList<User> cachedPapers = redissonSingle.getList(name);
        if (!cachedPapers.isExists()) {
            UserQueryRequest request = new UserQueryRequest();
            request.setUsername(name);
            List<User> papers = userService.list(request);
            if (papers.isEmpty()) {
                throw new ServiceException("f");
            }
            // 补偿进缓存，提高性能
            cachedPapers.addAll(papers);
            cachedPapers.expire(30, TimeUnit.DAYS);
        }
        int size = cachedPapers.size();
        int randomIndex = new Random().nextInt(size);
        return cachedPapers.get(randomIndex);
    }


    /**
     * 类上@Validated+方法上@Valid
     *
     * @return
     */
    @Operation(summary = "分页查询")
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

    @Operation(summary = "列表查询")
    @PostMapping("/list")
    public Result<List<User>> list(@RequestBody UserQueryRequest request) {
        //List<User> users = hystrixComponent.getUsers();
        List<User> users = userService.list(request);
        log.info("查询用户列表:{}", JSON.toJSONString(users));
        return ResultUtil.success(users);
    }

    @Operation(summary = "删除")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResultUtil.success(true);
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid UpdateUserRequest request) throws Exception {
        return ResultUtil.success(userService.update(request));
    }


    public static void main(String[] args) {

        UpdateUserRequest request = new UpdateUserRequest();
        request.setUsername("");
        request.setAge(25);
        System.out.println(ValidationUtil.validate(request));
    }


}
