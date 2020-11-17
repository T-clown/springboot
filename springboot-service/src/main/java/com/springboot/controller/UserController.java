package com.springboot.controller;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.springboot.common.DynamicDataSourceContextHolder;
import com.springboot.common.DynamicRoutingDataSource;
import com.springboot.common.entity.Result;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.DataSourceInfo;
import com.springboot.entity.UpdateUserRequest;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import com.springboot.common.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("/user")
//@ImportSelector(mode = CommonYN.NO)
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
    @PostMapping("/add")
    public Result add(@RequestBody @Valid CreateUserRequest request) {
        return ResultUtil.success(userService.addUser(request));
    }

    @Autowired
    private DynamicRoutingDataSource dynamicRoutingDataSource;
    @Autowired
    DataSourceInfo dataSourceInfo;

    /**
     * 类上@Validated+方法上@Valid
     *
     * @param id
     * @return
     */
    //@DataSource(name = "slave")
    @PostMapping("/{id}")
    public Result<User> getUserById(@Valid @PathVariable("id") @Max(value = 5, message = "超过 id 的范围了") Integer id) {
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
        @Valid @RequestParam("name") @Size(max = 6, message = "超过 username 的范围了") @NotBlank String name,
        @Valid @RequestParam("id") @NotNull(message = "id不能为空") Long id) {
        User user = new User();
        user.setUsername(name);
        user.setId(id);
        return ResultUtil.success(user);
    }

    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody @Valid UpdateUserRequest request) {
        return ResultUtil.success(userService.updateUser(request));
    }

}
