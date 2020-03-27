package com.springboot.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.springboot.validator.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 主键id
     */
    @NotNull(message = "id不能为空")
    private Long id;
    /**
     * 姓名
     */
    @Size(min = 5,max = 10,message = "姓名长度不对")
    @NotBlank(message = "姓名不能为空")
    private String username;

    @NotNull(message = "年龄不能为空")
    @Max(value = 100,message = "年龄超过100")
    @Min(value = 10,message = "年龄不低于10")
    private Integer age;

    @Past(message = "生日日期错误")
    private Date birthday;

    @Length(min = 3,max = 11,message = "手机号位数不对")
    private String phone ;

    @Email
    private String email;

    @Region(message = "region值不在范围内")
    private String region;
}
