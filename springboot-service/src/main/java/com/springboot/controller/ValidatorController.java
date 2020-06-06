package com.springboot.controller;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.springboot.annotation.DataSource;
import com.springboot.common.DynamicDataSourceContextHolder;
import com.springboot.common.DynamicRoutingDataSource;
import com.springboot.common.Result;
import com.springboot.common.ResultUtil;
import com.springboot.entity.CreateUserRequest;
import com.springboot.entity.DataSourceInfo;
import com.springboot.entity.User;
import com.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/validator")
public class ValidatorController {

    @Autowired
    UserService userService;

    @PostConstruct
    public void init(){
        System.out.println("初始化。。。。。。。。。");
    }

    /**
     * https://mp.weixin.qq.com/s/2RJqnJjwrDop4DTSnjV6yA
     * https://mp.weixin.qq.com/s/uOUAmdeX88Cv0mXvBtQTnQ
     *
     * @param request
     * @return 参数为对象的：方法上加@Valid 或者 @Validated
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody @Valid CreateUserRequest request) {
        return ResultUtil.success(userService.saveUser(request));
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
    @PostMapping("/user/{id}")
    public Result<User> getUserById(
        @Valid @PathVariable("id") @Max(value = 5, message = "超过 id 的范围了") Integer id) {
        if(id==3){
            dynamicRoutingDataSource.addDataSource(dataSourceInfo.getProperties());
            DynamicDataSourceContextHolder.setDataSourceKey(dataSourceInfo.getUrl());
        }
        return ResultUtil.success(userService.get(id));
    }

    /**
     * 类上@Validated+方法上@Valid
     *
     * @param name
     * @return
     */
    @PostMapping("/getUser")
    public Result<User> getUser(
        @Valid @RequestParam("username") @Size(max = 6, message = "超过 username 的范围了") String name,
        @Valid @RequestParam("id") @NotNull(message = "id不能为空") Long id) {
        User user = new User();
        user.setUsername(name);
        user.setId(id);
        return ResultUtil.success(user);
    }

}
