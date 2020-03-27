package com.springboot.entity.excel;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class DownloadData {
    @ExcelProperty("用户名")
    private String username;
    @ExcelProperty("性别")
    private String sex;
    @ExcelProperty("生日")
    private Date birthday;
    @ExcelProperty("邮箱")
    private String email;
    @ExcelProperty("手机")
    private String phone;
    @ExcelProperty("注册时间")
    private Date createTime;

}
