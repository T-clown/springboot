package com.springboot.utils.excel.poi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelSheet<T> {
    /**
     * sheet名称
     */
    private String sheetName;
    /**
     * 数据
     */
    private List<T> data;

}

