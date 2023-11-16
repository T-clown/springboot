package com.springboot.utils.excel.poi;

import lombok.Data;

import java.util.Date;

@Data
public class TestData {

    @ExcelColumn("账号")
    private String account;

    @ExcelColumn("姓名")
    private Long name;

}
