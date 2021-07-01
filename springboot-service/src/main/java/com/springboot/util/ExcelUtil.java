package com.springboot.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
public class ExcelUtil<T> {

    public static void exportExcel(HttpServletResponse response,
                                   Class head, List<?> listData, String fileName) throws IOException {
        if(CollectionUtils.isEmpty(listData)){
            return;
        }
        log.info("导出 Excel 数据 [{}], {} 条",fileName,listData.size());
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), head).sheet("sheet0").doWrite(listData);
    }

    public static <T> List<T> importExcel(MultipartFile file, Class<T> head) {
        List<T> data = new ArrayList<>();
        try (InputStream inputStream = new BufferedInputStream(file.getInputStream())) {
            ExcelListener<T> excelListener = new ExcelListener<>(data);
            //执行完，才执行listener
            EasyExcel.read(inputStream, head, excelListener).sheet().doRead();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
