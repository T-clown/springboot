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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.springboot.enums.GenderType;
import com.springboot.serializer.DateToLongSerializer;
import com.springboot.serializer.GenderDeserializer;
import com.springboot.serializer.LocalDateTimeToLongSerializer;
import com.springboot.validator.Region;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.validator.constraints.Length;

@Data
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

    @JsonSerialize(using = DateSerializer.class)
    private Date date = new Date();

    private Date date2 = null;

    @JsonSerialize(using = DateToLongSerializer.class)
    private Date date3;

    @JsonSerialize(using = DateToLongSerializer.class)
    private Date date4 = new Date();

    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonSerialize(using = LocalDateTimeToLongSerializer.class)
    private LocalDateTime createTime;

    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonSerialize(using = LocalDateTimeToLongSerializer.class)
    @JsonFormat
    private LocalDateTime updateTime;

}
