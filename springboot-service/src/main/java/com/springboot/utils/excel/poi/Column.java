package com.springboot.utils.excel.poi;

import lombok.Data;

/**
 * Sheet 列信息
 */
@Data
public class Column {
    /** 列关键字 */
    private String key;
    /** 列名 */
    private String name;
    /** 列宽 */
    private Integer width;

    public Column() {
    }

    public Column(String key, String name, int width) {
        this.key = key;
        this.name = name;
        this.width = width;
    }

    public Column(String key, String name) {
        this.key = key;
        this.name = name;
        this.width = 20;
    }
}