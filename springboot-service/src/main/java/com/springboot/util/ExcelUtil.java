package com.springboot.util;

import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
public class ExcelUtil {


    public static <T> void exportExcel(HttpServletResponse response,
                                   Class<T> head, List<T> listData, String fileName)  {
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

    /**
     *
     * @param file 上传文件
     * @param clazz 解析的类
     * @param converter 解析的类-->数据库DTO
     * @param consumer 插入数据
     * @param <T> 解析的类
     * @param <R> 数据库DTO
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
