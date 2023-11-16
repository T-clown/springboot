package com.springboot.utils.excel.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.springboot.utils.excel.poi.Column;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ExcelUtil {


    /**
     * 导出excel
     *
     * @param response
     * @param head
     * @param listData
     * @param fileName
     * @param <T>
     */
    public static <T> void exportExcel(HttpServletResponse response, Class<T> head, List<T> listData, String fileName) {
        if (CollectionUtils.isEmpty(listData)) {
            return;
        }
        try {
            log.info("导出 Excel 数据 [{}], {} 条", fileName, listData.size());
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), head).sheet("sheet0").doWrite(listData);
        } catch (IOException e) {
            log.error("Excel导出失败，Class:{}", head.getName(), e);
        }
    }

    public static void exportExcel2(HttpServletResponse response, List<EasyExcelSheet> sheets, String fileName) {
        if (CollectionUtils.isEmpty(sheets)) {
            return;
        }
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build()) {
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            for (int i = 0; i < sheets.size(); i++) {
                // 每次都要创建writeSheet 这里注意必须指定sheetNo 而且sheetName必须不一样
                EasyExcelSheet sheet = sheets.get(i);
                List<List<String>> headers = new ArrayList<>(sheet.getHeaders());
                List<Column> columns = sheet.getColumns();
                List<String> columnHeaders = columns.stream().map(Column::getName).collect(Collectors.toList());
                headers.add(columnHeaders);
                List<String> excludeColumns = sheet.getExcludeColumns();
                List<Map<String, Object>> data = sheet.getData();
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheet.getSheetName()).head(headers).excludeColumnFieldNames(excludeColumns).build();
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                List<List<Object>> downloadData = data.stream().map(x -> columns.stream().map(y -> x.get(y.getKey())).collect(Collectors.toList())).collect(Collectors.toList());
                excelWriter.write(downloadData, writeSheet);
            }
        } catch (IOException e) {
            log.error("Excel导出失败，fileName:{}", fileName, e);
        }
    }

    /**
     * @param file      上传文件
     * @param clazz     解析的类
     * @param converter 解析的类-->数据库DTO
     * @param consumer  插入数据
     * @param <T>       解析的类
     * @param <R>       数据库DTO
     */
    public static <T, R> void upload(MultipartFile file, Class<T> clazz, Function<T, R> converter, Consumer<List<R>> consumer) {
        try {
            EasyExcel.read(file.getInputStream(), clazz, new ExcelListener<>(converter, consumer)).sheet()
                    .doRead();
        } catch (IOException e) {
            log.error("Excel导入失败，Class:{}", clazz.getName(), e);
        }
    }

}
