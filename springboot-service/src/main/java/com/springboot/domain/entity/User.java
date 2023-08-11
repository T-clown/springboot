package com.springboot.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.springboot.common.serializer.DateToLongSerializer;
import com.springboot.common.serializer.GenderDeserializer;
import com.springboot.common.serializer.LocalDateTimeToLongSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
