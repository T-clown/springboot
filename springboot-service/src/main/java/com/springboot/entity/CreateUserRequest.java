package com.springboot.entity;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.springboot.annotation.LockKeyParam;
import com.springboot.enums.GenderType;
import com.springboot.serializer.GenderDeserializer;
import com.springboot.validator.Region;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateUserRequest {

    /**
     * 姓名
     */
    @Length(min = 1, max = 10, message = "姓名太长")
    @NotBlank(message = "姓名不能为空")
    private String username;

    //@NotNull(message = "年龄不能为空")
    @Max(value = 100, message = "年龄超过100")
    @Min(value = 10, message = "年龄不低于10")
    private Integer age;

    @Past(message = "生日日期错误")
    private Date birthday;

    @Length(min = 11, max = 11, message = "手机号位数不对")
    private String phone;

    @NotNull(message = "性别不能为空")
    //@JsonDeserialize(using = GenderDeserializer.class)
    //private GenderType gender;
    private String gender;

    @Email
    private String email;

    @Region(message = "region值不在范围内")
    private String region;
}
