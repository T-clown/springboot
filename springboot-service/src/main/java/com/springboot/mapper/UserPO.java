package com.springboot.mapper;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author macbookpro
 */
@Getter
@Setter
@TableName("springboot_user")
public class UserPO {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String gender;

    private LocalDateTime birthday;

    private String email;

    private String phone;

    private String region;

    private String status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Short isDeleted;
}
