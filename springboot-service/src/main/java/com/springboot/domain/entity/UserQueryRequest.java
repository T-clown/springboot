package com.springboot.domain.entity;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
@Validated
@Data
public class UserQueryRequest {

    private String username;

    //@NotNull(message = "年龄不能为空")
    @Max(value = 100, message = "年龄超过100")
    @Min(value = 1, message = "年龄不低于1")
    private Integer age;


    @Length(min = 11, max = 11, message = "手机号位数不对")
    private String phone;

    private String gender;

    @Email(message = "邮箱格式不正确")
    private String email;


}
