package com.springboot.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.springboot.enums.GenderType;
import com.springboot.serializer.GenderDeserializer;
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
    private Integer id;
    /**
     * 姓名
     */
    private String username;


    private Integer age;


    private String phone;

    @JsonDeserialize(using = GenderDeserializer.class)
    private String gender;


    private String email;


    private String region;

    private LocalDateTime birthday;

    private Short status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
