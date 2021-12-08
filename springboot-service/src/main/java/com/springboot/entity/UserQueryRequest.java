package com.springboot.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

    @Email
    private String email="5666666";


}
