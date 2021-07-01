package com.springboot.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.excel.EasyExcel;

import com.google.common.collect.Lists;
import com.springboot.common.entity.Result;
import com.springboot.common.enums.ResultCode;
import com.springboot.common.exception.ServiceRuntimeException;
import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.excel.DownloadData;
import com.springboot.entity.excel.UploadData;
import com.springboot.util.BeanCopyUtils;
import com.springboot.util.ExcelUploadListener;
import com.springboot.service.repository.UserRepository;
import com.springboot.util.ExcelUtil;
import com.springboot.common.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    UserRepository userRepository;

    /**
     * @Desc 批量导出
     **/
    @PostMapping(value = "/export/user")
    public Result exportUser(HttpServletResponse response) {
        try {
            List<UserDTO> userDTOS = userRepository.getUserDTOS();
            List<DownloadData> data = userDTOS.stream().map(x -> BeanCopyUtils.copyProperties(x, DownloadData.class)).collect(Collectors.toList());
            ExcelUtil.exportExcel(response, DownloadData.class, data, "用户信息");
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("导出用户信息失败", e);
            throw new ServiceRuntimeException(ResultCode.INVALID_PARAMETER);
        }
    }

    /**
     * @Desc 批量导入
     **/
    @PostMapping(value = "/import/user", headers = "content-type=multipart/form-data")
    public Result importUser(@RequestParam MultipartFile file) {
        try {
            List<UploadData> importDatas = ExcelUtil.importExcel(file,
                UploadData.class);
            userRepository.addUser(importDatas.stream().map(this::convert).collect(Collectors.toList()));
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("用户导入流关闭失败", e);
            return ResultUtil.error(ResultCode.UNKNOWN_ERROR, e.getMessage());
        }
    }

    /**
     * 文件上传
     * 1. 创建excel对应的实体对象 参照{@link UploadData}
     * 2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link ExcelUploadListener}
     * 3. 直接读即可
     */
    @PostMapping("/upload/user")
    public Result upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UploadData.class, new ExcelUploadListener<>(this::convert, t -> userRepository.addUser(t))).sheet()
            .doRead();
        return ResultUtil.success();
    }

    private UserDTO convert(UploadData uploadData){
       return BeanCopyUtils.copyProperties(uploadData,UserDTO.class);
    }
}
