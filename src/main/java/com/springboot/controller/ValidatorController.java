package com.springboot.controller;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.springboot.common.Result;
import com.springboot.common.ResultUtil;
import com.springboot.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * redis限流
 */
@RestController
@RequestMapping("/validator")
@Validated
public class ValidatorController {

    /**
     * https://mp.weixin.qq.com/s/2RJqnJjwrDop4DTSnjV6yA
     * https://mp.weixin.qq.com/s/uOUAmdeX88Cv0mXvBtQTnQ
     *
     * @param user
     * @return 参数为对象的：方法上加@Valid 或者 @Validated
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody @Valid User user) {
        return ResultUtil.success(user);
    }

    /**
     * 类上@Validated+方法上@Valid
     *
     * @param id
     * @return
     */
    @PostMapping("/user/{id}")
    public ResponseEntity<User> getUserById(
        @Valid @PathVariable("id") @Max(value = 5, message = "超过 id 的范围了") Long id) {
        User user = new User();
        user.setId(id);
        return ResponseEntity.ok().body(user);
    }

    /**
     * 类上@Validated+方法上@Valid
     *
     * @param name
     * @return
     */
    @PostMapping("/getUser")
    public Result<User> getUser(@Valid @RequestParam("name") @Size(max = 6, message = "超过 name 的范围了") String name,
                                @Valid @RequestParam("id") @NotNull(message = "id不能为空") Long id) {
        User user = new User();
        user.setName(name);
        user.setId(id);
        return ResultUtil.success(user);
    }

}
