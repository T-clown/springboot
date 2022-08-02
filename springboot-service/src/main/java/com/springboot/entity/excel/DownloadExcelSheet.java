package com.springboot.entity.excel;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DownloadExcelSheet {

    private String sheetName;

    private List<Column> columns;

    private List<Map<String, Object>> data;
}
