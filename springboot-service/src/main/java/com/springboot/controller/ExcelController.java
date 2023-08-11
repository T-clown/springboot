package com.springboot.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.springboot.common.entity.Result;
import com.springboot.common.utils.ResultUtil;
import com.springboot.dao.dto.UserDTO;
import com.springboot.domain.entity.UserQueryRequest;
import com.springboot.domain.entity.excel.DownloadData;
import com.springboot.domain.entity.excel.UploadData;
import com.springboot.domain.entity.excel.UploadTestData;
import com.springboot.service.repository.UserRepository;
import com.springboot.utils.BeanCopyUtils;
import com.springboot.utils.excel.easyexcel.ExcelUtil;
import com.springboot.utils.PPTToImageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Tag(name = "Excel导入导出")
@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {


    @Autowired
    private UserRepository userRepository;

    /**
     * @Desc 批量导出
     **/
    @Operation(summary = "批量导出用户")
    @PostMapping(value = "/export/user")
    public void exportUser(@RequestBody UserQueryRequest request, HttpServletResponse response) {
        List<UserDTO> userDTOS = userRepository.list(request);
        List<DownloadData> data = userDTOS.stream().map(x -> BeanCopyUtils.copyProperties(x, DownloadData.class)).collect(Collectors.toList());
        ExcelUtil.exportExcel(response, DownloadData.class, data, "用户信息");
    }

    @PostMapping(value = "/export/user2")
    public void exportUser2(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        //String fileName = URLEncoder.encode("测试", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String fileName = URLEncoder.encode("测试", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream()).head(head()).sheet("模板").doWrite(dataList());
    }


    @PostMapping(value = "/download")
    public Result<Void> download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        //response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        //response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        //String fileName = URLEncoder.encode("测试", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        String fileName = URLEncoder.encode("测试", StandardCharsets.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream()).head(head()).sheet("模板").doWrite(dataList());
        //AbstractMessageConverterMethodProcessor
        return ResultUtil.success();
    }

    private List<List<String>> head() {
        List<List<String>> list = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add("字符串" + System.currentTimeMillis());
        head0.add("字符串" +"aaa");
        List<String> head1 = ListUtils.newArrayList();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = ListUtils.newArrayList();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            List<Object> data = ListUtils.newArrayList();
            data.add("字符串" + i);
            data.add(new Date());
            data.add(0.56);
            list.add(data);
        }
        return list;
    }
    /**
     * 文件上传
     */
    @PostMapping("/upload/user")
    public Result<Void> upload(MultipartFile file) {
        ExcelUtil.upload(file, UploadData.class, this::convert, t -> userRepository.addUser(t));
        return ResultUtil.success();
    }

    @PostMapping("/upload/test")
    public Result<Void> uploadTest(MultipartFile file) {
        ExcelUtil.upload(file, UploadTestData.class, this::convert, this::convert);
        return ResultUtil.success();
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload/ppt")
    public Result<Void> pptToImage(MultipartFile file) throws IOException {
        String path = "/Users/hrtps/tmp";
        File file1=new File("/Users/hrtps/Desktop/测试.pdf");
        PPTToImageUtil.convertPdf2Png(file1, path);
        return ResultUtil.success();
    }

    private UserDTO convert(UploadData uploadData) {
        return BeanCopyUtils.copyProperties(uploadData, UserDTO.class);
    }

    private UploadTestData convert(UploadTestData uploadData) {
        return uploadData;
    }

    private void convert(List<UploadTestData> uploadData) {
        log.info(JSON.toJSONString(uploadData));
    }
}
