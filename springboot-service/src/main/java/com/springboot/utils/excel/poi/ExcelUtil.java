package com.springboot.utils.excel.poi;

import cn.hutool.core.util.RandomUtil;
import com.springboot.common.enums.ResultCode;
import com.springboot.common.exception.ServiceException;
import com.springboot.utils.LocalDateTimeUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class ExcelUtil {

    private static final String SHEET_NAME_CHAR_FORBID = "[\\\\/:：?？\\[\\]*']";

    public static String validSheetName(String source) {
        if (source == null || source.isBlank()) {
            return RandomUtil.randomString(10);
        }
        String target = source.replaceAll(SHEET_NAME_CHAR_FORBID, " ");
        if (target.length() > 31) {
            target = target.substring(0, 31);
        }
        return target;
    }

    /**
     * 这段代码可以加到POIExcelUtil里面去
     *
     * @param response
     * @param sheets
     */
    public static <T> void download(HttpServletResponse response, List<DownloadExcelSheet<T>> sheets, String fileName) {
        fileName = StringUtils.isBlank(fileName) ? LocalDateTimeUtil.format(LocalDateTime.now()) : fileName;
        try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook()) {
            if (CollectionUtils.isNotEmpty(sheets)) {
                for (DownloadExcelSheet<T> x : sheets) {
                    String sheetName = validSheetName(x.getSheetName());
                    List<Column> columns = x.getColumns();
                    List<TableCell<Object>> headers = x.getHeaders();
                    List<T> data = x.getData();
                    POIExcelUtil.exportExcel2007(xssfWorkbook, sheetName, columns, data, headers);
                }
            } else {
                xssfWorkbook.createSheet("无记录");
            }
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");
            xssfWorkbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new ServiceException(ResultCode.EXCEL_DOWNLOAD_ERROR);
        }
    }

    public static <T> void enhanceDownload(HttpServletResponse response, List<DownloadExcelSheet<T>> sheets, String fileName) {
        fileName = StringUtils.isBlank(fileName) ? LocalDateTimeUtil.format(LocalDateTime.now()) : fileName;
        try (SXSSFWorkbook xssfWorkbook = new SXSSFWorkbook(1000)) {
            if (CollectionUtils.isNotEmpty(sheets)) {
                for (DownloadExcelSheet<T> x : sheets) {
                    String sheetName = validSheetName(x.getSheetName());
                    List<Column> columns = x.getColumns();
                    List<T> data = x.getData();
                    List<TableCell<Object>> headers = x.getHeaders();
                    POIExcelUtil.exportExcel2007(xssfWorkbook, sheetName, columns, data, headers);
                }
            } else {
                xssfWorkbook.createSheet("无记录");
            }
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8) + ".xlsx");
            xssfWorkbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new ServiceException(ResultCode.EXCEL_DOWNLOAD_ERROR);
        }
    }
}
