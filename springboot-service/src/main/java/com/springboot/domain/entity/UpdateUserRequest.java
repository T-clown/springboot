package com.springboot.domain.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.springboot.domain.enums.GenderType;
import com.springboot.common.serializer.GenderDeserializer;
import com.springboot.common.validator.Region;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Schema(description = "张三")
@Data
public class UpdateUserRequest {
    @NotNull(message = "id不能为空")
    @PositiveOrZero(message = "不能为负数")
    private Long id;
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    //@Length(min = 1,max = 10, message = "length太大")
    @Size(min = 1,max = 50,message = "size太大")
    private String username;

    //@NotNull(message = "年龄不能为空")
    @Max(value = 100, message = "年龄超过100")
    @Min(value = 10, message = "年龄不低于1")
    private Integer age;

    @Past(message = "生日日期错误")
    private LocalDateTime birthday;

    @Length(min = 11, max = 11, message = "手机号位数不对")
    @NotBlank(message = "手机号不能为空")
    private String phone;

//    @NotBlank(message = "性别不能为空")
//    private String gender;

    @JsonDeserialize(using = GenderDeserializer.class)
    private GenderType gender;

    @Email(message = "邮箱格式错误")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Region(message = "region值不在范围内")
    private String region;
}
