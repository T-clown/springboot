package com.springboot.util;

import com.springboot.common.enums.ResultCode;
import com.springboot.common.exception.ServiceRuntimeException;
import com.springboot.entity.excel.Column;
import com.springboot.entity.excel.DownloadExcelSheet;
import com.springboot.entity.excel.TableCell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * HSSF：是操作Excel97-2003版本，扩展名为.xls。
 * XSSF：是操作Excel2007版本开始，扩展名为.xlsx。
 * SXSSF：是在XSSF基础上，POI3.8版本开始提供的一种支持低内存占用的操作方式，扩展名为.xlsx
 */
@Slf4j
public class POIExcelUtil {

    // 2007 版本以上 最大支持1048576行
    public final static String EXCEl_FILE_2007 = "2007";
    // 2003 版本 最大支持65536 行
    public final static String EXCEL_FILE_2003 = "2003";

    /**
     * <p>
     * 导出带有头部标题行的Excel <br>
     * 时间格式默认：yyyy-MM-dd hh:mm:ss <br>
     * </p>
     *
     * @param version 2003 或者 2007，不传时默认生成2003版本
     */
    public static <T> void exportExcel(String fileName, String sheetName, List<Column> columns, List<T> dataset, HttpServletResponse response, String version) {
        exportExcel(fileName, sheetName, columns, dataset, response, version, null, false);
    }

    public static <T> void exportExcel(String fileName, List<String> sheetNames, List<List<Column>> columns, List<List<T>> dataset,
                                       HttpServletResponse response, String version, List<List<TableCell<Object>>> headers,
                                       boolean removeFormat) {
        try {
            // 对外暴露 "Content-Disposition"
            // https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Access-Control-Expose-Headers
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
            response.setContentType("application/vnd.ms-excel");
            if (StringUtils.isBlank(version) || EXCEL_FILE_2003.equals(version.trim())) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xls");
                HSSFWorkbook workbook = new HSSFWorkbook();
                for (int i = 0; i < sheetNames.size(); i++) {
                    String sheetName = sheetNames.get(i);
                    List<Column> sheetColumns = columns.get(i);
                    List<T> sheetDataSet = dataset.get(i);
                    exportExcel2003(sheetName, sheetColumns, sheetDataSet, workbook);
                }
                workbook.write(response.getOutputStream());
            } else {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook();
                for (int i = 0; i < sheetNames.size(); i++) {
                    String sheetName = sheetNames.get(i);
                    List<Column> sheetColumns = columns.get(i);
                    List<T> sheetDataSet = dataset.get(i);
                    List<TableCell<Object>> sheetHeaders = (headers != null && headers.size() > i) ? headers.get(i) : null;
                    exportExcel2007(workbook, sheetName, sheetColumns, sheetDataSet, sheetHeaders);
                    removeFormat(removeFormat, workbook);
                }
                workbook.write(response.getOutputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void exportExcel(String fileName, String sheetName, List<Column> columns, List<T> dataset,
                                       HttpServletResponse response, String version, List<TableCell<Object>> headers,
                                       boolean removeFormat) {
        exportExcel(fileName, Collections.singletonList(sheetName), Collections.singletonList(columns),
                Collections.singletonList(dataset), response, version, Collections.singletonList(headers), removeFormat);
    }

    private static void removeFormat(boolean removeFormat, XSSFWorkbook workbook) {
        if (removeFormat) {
            DataFormat fmt = workbook.createDataFormat();
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setDataFormat(fmt.getFormat("@"));
            for (Row row : workbook.getSheetAt(0)) {
                for (Cell cell : row) {
                    cell.setCellStyle(cellStyle);
                }
            }
        }
    }


    public static <T> HSSFWorkbook exportExcel2003(String sheetName, List<Column> columns, List<T> data) {
        return exportExcel2003(sheetName, getColumnNames(columns), getColumnKeys(columns), listConvert(data));
    }

    public static <T> HSSFWorkbook exportExcel2003(String sheetName, List<Column> columns, List<T> data, HSSFWorkbook workbook) {
        return exportExcel2003(sheetName, getColumnNames(columns), getColumnKeys(columns), listConvert(data), workbook);
    }

    public static <T> XSSFWorkbook exportExcel2007(XSSFWorkbook wb, String sheetName, List<Column> columns, List<T> data) {
        return exportExcel2007(wb, sheetName, getColumnNames(columns), getColumnKeys(columns), listConvert(data), null);
    }

    public static <T> XSSFWorkbook exportExcel2007(XSSFWorkbook wb, String sheetName, List<Column> columns, List<T> data,
                                                   List<TableCell<Object>> headers) {
        return exportExcel2007(wb, sheetName, getColumnNames(columns), getColumnKeys(columns), listConvert(data), headers);
    }

    /**
     * 使用SXSSFWorkbook 替换XSSFWorkbook,使用poi 流式下载
     */
    public static <T> SXSSFWorkbook exportExcel2007(SXSSFWorkbook wb, String sheetName, List<Column> columns, List<T> data) {
        return exportExcel2007(wb, sheetName, getColumnNames(columns), getColumnKeys(columns), listConvert(data));
    }

    /**
     * 使用SXSSFWorkbook 替换XSSFWorkbook,使用poi 流式下载
     */
    public static SXSSFWorkbook exportExcel2007(SXSSFWorkbook workbook, String sheetName, String[] columnNames, String[] columnKeys,
                                                List<Map<String, ?>> rows) {
        // 声明一个工作薄
        if (workbook == null) {
            workbook = new SXSSFWorkbook(1000);
        }
        // 生成一个表格
        SXSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);

        // 创建表格标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        // 创建表格标题字体
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setColor(XSSFFont.COLOR_NORMAL);
        font.setFontHeightInPoints((short) 11);
        headerStyle.setFont(font);

        // 创建表格标题行
        Row row = sheet.createRow(0);
        Cell cellHeader;
        for (int i = 0; i < columnNames.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(headerStyle);
            cellHeader.setCellValue(new XSSFRichTextString(columnNames[i]));
        }

        // 生成表格数据部分样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFillForegroundColor(IndexedColors.WHITE1
                .getIndex());
        dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 默认字体
        Font dataFont = workbook.createFont();
        dataStyle.setFont(dataFont);

        // 插入业务数据
        int headerCount = 0;
        Cell cell;
        if (rows != null && rows.size() > 0) {
            // 遍历列表
            for (int i = 0; i < rows.size(); i++) {
                // 获取行数据
                Map<String, ?> columns = rows.get(i);
                row = sheet.createRow(i + 1 + headerCount);// 创建行
                row.setHeight((short) 320);

                // 设置列
                for (int j = 0; j < columnKeys.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(dataStyle);
                    if (columns.get(columnKeys[j]) != null) {
                        Object value = columns.get(columnKeys[j]);
                        if (value instanceof Date) {
                            cell.setCellValue(LocalDateTimeUtil.format((Date) value));
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value ? "是" : "否");
                        } else {
                            cell.setCellValue(columns.get(columnKeys[j]).toString());
                        }
                    }
                }
            }
        }
        return workbook;
    }

    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此版本生成2007以上版本的文件 (文件后缀：xlsx)
     * </p>
     */
    public static XSSFWorkbook  exportExcel2007(XSSFWorkbook workbook, String sheetName, String[] columnNames, String[] columnKeys,
                                               List<Map<String, ?>> rows, List<TableCell<Object>> headers) {
        // 声明一个工作薄
        if (workbook == null) {
            workbook = new XSSFWorkbook();
        }
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);

        // 创建表格标题样式
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(new XSSFColor());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        // 创建表格标题字体
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("宋体");
        font.setColor(new XSSFColor());
        font.setFontHeightInPoints((short) 11);
        headerStyle.setFont(font);

        // 表头
        int headerCount = 0;
        if (CollectionUtils.isNotEmpty(headers)) {
            // 根据行group
            Map<Integer, List<TableCell<Object>>> rowNumCellsMap = headers.stream()
                    .collect(Collectors.groupingBy(TableCell::getRow));
            rowNumCellsMap.forEach((row, cells) -> {
                // 创建行
                XSSFRow tableRow = sheet.createRow(row);
                cells.forEach(tableCell -> {
                    XSSFCell cell = tableRow.createCell(tableCell.getColumn());
                    setCellValue(cell, tableCell.getValue());
                    // 合并单元格
                    tableCell.setEndRow(tableCell.getEndRow() == null ? tableCell.getRow() : tableCell.getEndRow());
                    tableCell.setEndColumn(tableCell.getEndColumn() == null ? tableCell.getColumn() :
                            tableCell.getEndColumn());
                    if (!Objects.equals(tableCell.getColumn(), tableCell.getEndColumn()) || !Objects.equals(tableCell.getRow(), tableCell.getEndRow())) {
                        sheet.addMergedRegion(new CellRangeAddress(tableCell.getRow(), tableCell.getEndRow(),
                                tableCell.getColumn(), tableCell.getEndColumn()));
                    }
                });
            });
            headerCount = sheet.getLastRowNum() + 1;
        }

        // 创建表格标题行
        XSSFRow row = sheet.createRow(headerCount);
        XSSFCell cellHeader;
        for (int i = 0; i < columnNames.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(headerStyle);
            cellHeader.setCellValue(new XSSFRichTextString(columnNames[i]));
        }

        // 生成表格数据部分样式
        XSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFillForegroundColor(new XSSFColor());
        dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        XSSFFont dataFont = workbook.createFont();
        dataStyle.setFont(dataFont);

        // 插入业务数据
        XSSFCell cell;
        if (rows != null && rows.size() > 0) {
            // 遍历列表
            for (int i = 0; i < rows.size(); i++) {
                // 获取行数据
                Map<String, ?> columns = rows.get(i);
                row = sheet.createRow(i + 1 + headerCount);// 创建行
                row.setHeight((short) 320);

                // 设置列
                for (int j = 0; j < columnKeys.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(dataStyle);
                    if (columns.get(columnKeys[j]) != null) {
                        Object value = columns.get(columnKeys[j]);
                        setCellValue(cell, value);
                    }
                }
            }
        }
        return workbook;
    }

    private static void setCellValue(XSSFCell cell, Object value) {
        if (value instanceof Date) {
            cell.setCellValue(LocalDateTimeUtil.format((Date) value));
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value ? "是" : "否");
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * <p>
     * 通用Excel导出方法,利用反射机制遍历对象的所有字段，将数据写入Excel文件中 <br>
     * 此方法生成2003版本的excel,文件名后缀：xls <br>
     * </p>
     * 如果有时间数据，设定输出格式。默认为"yyyy-MM-dd hh:mm:ss"
     */
    public static HSSFWorkbook exportExcel2003(String sheetName, String[] columnNames, String[] columnKeys,
                                               List<Map<String, ?>> rows, HSSFWorkbook workbook) {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(20);
        // 生成一个样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        // 设置标题这些样式
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        // 生成标题字体
        HSSFFont font = workbook.createFont();
        //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        // 把字体应用到当前的样式
        headerStyle.setFont(font);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cellHeader;
        for (int i = 0; i < columnNames.length; i++) {
            cellHeader = row.createCell(i);
            cellHeader.setCellStyle(headerStyle);
            cellHeader.setCellValue(new HSSFRichTextString(columnNames[i]));
        }

        // 生成表格数据样式
        HSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成表格数据字体
        HSSFFont dataFont = workbook.createFont();
        //dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        dataStyle.setFont(dataFont);

        // 插入业务数据
        int headerCount = 0;
        HSSFCell cell;
        if (rows != null && rows.size() > 0) {
            // 遍历列表
            for (int i = 0; i < rows.size(); i++) {
                // 获取行数据
                Map<String, ?> columns = rows.get(i);
                row = sheet.createRow(i + 1 + headerCount);// 创建行
                row.setHeight((short) 320);

                // 设置列
                for (int j = 0; j < columnKeys.length; j++) {
                    cell = row.createCell(j);
                    cell.setCellStyle(dataStyle);
                    if (columns.get(columnKeys[j]) != null) {
                        Object value = columns.get(columnKeys[j]);
                        if (value instanceof Date) {
                            cell.setCellValue(LocalDateTimeUtil.format((Date) value));
                        } else if (value instanceof Boolean) {
                            cell.setCellValue((Boolean) value ? "是" : "否");
                        } else {
                            cell.setCellValue(columns.get(columnKeys[j]).toString());
                        }
                    }
                }
            }
        }
        return workbook;
    }

    public static <T> HSSFWorkbook exportExcel2003(String sheetName, String[] columnNames, String[] columnKeys,
                                                   List<Map<String, ?>> rows) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        exportExcel2003(sheetName, columnNames, columnKeys, rows, workbook);
        return workbook;
    }

    private static <T> List<Map<String, ?>> listConvert(List<T> datas) {
        List<Map<String, ?>> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(datas)) {
            for (T data : datas) {
                Map<String, ?> dataMap = null;
                if (data instanceof Map) {
                    dataMap = (Map<String, ?>) data;
                } else {
                    try {
                        dataMap = PropertyUtils.describe(data);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        log.error("list to map convert error:{}", e.getMessage());
                    }
                }
                result.add(dataMap);
            }
        }
        return result;
    }

    private static String[] getColumnKeys(List<Column> columns) {
        return columns.stream().map(Column::getKey).collect(Collectors.toList()).toArray(String[]::new);
    }

    private static String[] getColumnNames(List<Column> columns) {
        return columns.stream().map(Column::getName).collect(Collectors.toList()).toArray(String[]::new);
    }

    private static Integer[] getColumnWidths(List<Column> columns) {
        return columns.stream().map(Column::getWidth).collect(Collectors.toList()).toArray(Integer[]::new);
    }

    public static void exportExcel2007(HttpServletResponse response, List<DownloadExcelSheet> sheets, String fileName) {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        if (CollectionUtils.isNotEmpty(sheets)) {
            for (DownloadExcelSheet sheet : sheets) {
                String sheetName = sheet.getSheetName();
                List<Column> columns = sheet.getColumns();
                List<Map<String, Object>> data = sheet.getData();
                exportExcel2007(xssfWorkbook, sheetName, columns, data);
            }
        } else {
            xssfWorkbook.createSheet("无记录");
        }
        try {
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");
            xssfWorkbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new ServiceRuntimeException(ResultCode.FAILURE);
        }
    }

    public static void main(String[] args) {
        List<Column> columns = new ArrayList<>();
        columns.add(new Column("name", "姓名"));

        List<Column> data = new ArrayList<>();
        data.add(new Column("name", "张"));

        SXSSFWorkbook wb = exportExcel2007(new SXSSFWorkbook(), "学生数据", columns, data);
        try {
            String fileName = "/Users/hrtps/Desktop/test.xlsx";
            System.out.println(fileName);
            FileOutputStream fout = new FileOutputStream(fileName);
            wb.write(fout);
            fout.close();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
