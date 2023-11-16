package com.springboot.utils.excel.poi;

import com.google.common.collect.Maps;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ExcelParser<T> {

    private final Class<T> type;

    public ExcelParser(Class<T> type) {
        this.type = type;
    }

    public List<ExcelSheet<T>> parse(InputStream is) {
        List<ExcelSheet<T>> workbooks = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            int sheets = workbook.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                ExcelSheet<T> workbookData = parseSheet(sheet);
                workbooks.add(workbookData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return workbooks;
    }

    public void parse(InputStream is, Consumer<List<ExcelSheet<T>>> consumer) {
        List<ExcelSheet<T>> workbooks = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(is)) {
            int sheets = workbook.getNumberOfSheets();
            for (int i = 0; i < sheets; i++) {
                Sheet sheet = workbook.getSheetAt(i);

                ExcelSheet<T> workbookData = parseSheet(sheet);
                workbooks.add(workbookData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        consumer.accept(workbooks);
    }

    private ExcelSheet<T> parseSheet(Sheet sheet) throws Exception {
        //表头
        Row headerRow = sheet.getRow(0);

        int colCount = headerRow.getPhysicalNumberOfCells();

        Field[] fields = type.getDeclaredFields();
        String[] columnNames = new String[colCount];

        for (int j = 0; j < colCount; j++) {
            Cell cell = headerRow.getCell(j);
            columnNames[j] = cell.getStringCellValue();
        }

        List<T> dataList = new ArrayList<>();

        Constructor<T> constructor = type.getConstructor();

        Map<String, Field> columnNameFieldMap = Maps.newHashMap();
        for (Field field : fields) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            if (excelColumn != null) {
                columnNameFieldMap.put(excelColumn.value(), field);
            }
        }

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }
            T instance = constructor.newInstance();
            for (int j = 0; j < colCount; j++) {
                Cell cell = row.getCell(j);

                String columnName = columnNames[j];
                Field field = columnNameFieldMap.get(columnName);
                if (field == null) {
                    continue;
                }
                field.setAccessible(true);
                field.set(instance, getValue(field.getType(), cell));
            }
            dataList.add(instance);
        }

        return new ExcelSheet<>(sheet.getSheetName(), dataList);
    }

    private static Object getValue(Class<?> clazz, Cell cell) {
        if (clazz == String.class) {
            // 将单元格内容强制转换为字符串
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue();
        }
        if (clazz == Date.class) {
            return cell.getDateCellValue();
        }
        if (clazz == LocalDateTime.class) {
            return cell.getLocalDateTimeCellValue();
        }
        if (clazz == Boolean.class) {
            return cell.getBooleanCellValue();
        }
        if (clazz == double.class || clazz == Double.class) {
            return cell.getNumericCellValue();
        }
        if (clazz == float.class || clazz == Float.class) {
            return (float) cell.getNumericCellValue();
        }
        if (clazz == BigDecimal.class) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        if (clazz == long.class || clazz == Long.class) {
            return (long) cell.getNumericCellValue();
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (int) cell.getNumericCellValue();
        }
        if (clazz == short.class || clazz == Short.class) {
            return (short) cell.getNumericCellValue();
        }
        if (clazz == byte.class || clazz == Byte.class) {
            return (byte) cell.getNumericCellValue();
        }
        return null;
    }


}


