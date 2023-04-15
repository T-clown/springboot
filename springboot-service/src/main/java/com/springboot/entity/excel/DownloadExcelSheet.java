package com.springboot.entity.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadExcelSheet<T> {

    private String sheetName;

    private List<Column> columns;

    private List<TableCell<Object>> headers;

    private List<T> data;
}
