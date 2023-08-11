package com.springboot.domain.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class UploadTestData {
    @ExcelProperty("场景")
    private String groupName;
    @ExcelProperty("情境")
    private String unitName;
    @ExcelProperty("题组名称")
    private String roundName;
    @ExcelProperty("题组描述")
    private String roundDesc;
    @ExcelProperty("题目名称")
    private String questionName;
    @ExcelProperty("标准答案")
    private String answer;
    @ExcelProperty("关键词")
    private String keyword;

}
