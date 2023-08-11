package com.springboot.utils.excel.poi;

import lombok.Data;

@Data
public class TableCell<T> {

    private Integer row;

    private Integer endRow;

    private Integer column;

    private Integer endColumn;

    private T value;

    public TableCell(Integer row, Integer column, T value) {
        this.row = row;
        this.endRow = row;
        this.column = column;
        this.endColumn = column;
        this.value = value;
    }

    public TableCell(Integer row, Integer endRow, Integer column, Integer endColumn, T value) {
        this.row = row;
        this.endRow = endRow;
        this.column = column;
        this.endColumn = endColumn;
        this.value = value;
    }
}
