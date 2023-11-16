package com.springboot.utils.excel.easyexcel;

import com.springboot.utils.excel.poi.Column;
import com.springboot.utils.excel.poi.TableCell;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;
import org.checkerframework.checker.units.qual.C;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EasyExcelSheet {

    private String sheetName;

    private List<List<String>> headers;

    private List<Column> columns;

    private List<String> excludeColumns;

    private List<Map<String, Object>> data;
}
