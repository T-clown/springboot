package com.springboot.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;

import org.springframework.web.multipart.MultipartFile;

public class ExcelUtil<T> {

    public static void exportExcel(HttpServletResponse response,
        Class head, List<?> listData, String fileName) throws IOException {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), head).sheet("sheet0").doWrite(listData);
    }


    public static List<?> importExcel(MultipartFile file, Class<?> head) {
        InputStream inputStream = null;
        List<?> data = new ArrayList<> ();
        try {
            inputStream = new BufferedInputStream ( file.getInputStream () );
            ExcelListener excelListener = new ExcelListener ( data );
            //执行完，才执行listener
            EasyExcel.read ( inputStream, head, excelListener ).sheet ().doRead ();
            return data;
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close ();
                } catch (IOException e) {
                    e.printStackTrace ();
                }
            }
        }
        return data;
    }
}
