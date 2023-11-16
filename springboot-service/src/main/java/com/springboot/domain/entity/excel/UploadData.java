package com.springboot.domain.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UploadData {
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("性别")
    private String gender;
    @ExcelProperty("生日")
    private Date birthday;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("手机")
    private String phone;
    @ExcelProperty("注册时间")
    private LocalDateTime createTime;

}
