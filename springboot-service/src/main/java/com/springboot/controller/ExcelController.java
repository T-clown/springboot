package com.springboot.controller;

import com.springboot.common.entity.Result;
import com.springboot.common.util.ResultUtil;
import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.UserQueryRequest;
import com.springboot.entity.excel.DownloadData;
import com.springboot.entity.excel.UploadData;
import com.springboot.service.repository.UserRepository;
import com.springboot.util.BeanCopyUtils;
import com.springboot.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

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
    public void exportUser(@RequestBody UserQueryRequest request, HttpServletResponse response) {
        List<UserDTO> userDTOS = userRepository.getUserDTOS(request);
        List<DownloadData> data = userDTOS.stream().map(x -> BeanCopyUtils.copyProperties(x, DownloadData.class)).collect(Collectors.toList());
        ExcelUtil.exportExcel(response, DownloadData.class, data, "用户信息");
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload/user")
    public Result upload(MultipartFile file) {
        ExcelUtil.upload(file, UploadData.class, this::convert, t -> userRepository.addUser(t));
        return ResultUtil.success();
    }

    private UserDTO convert(UploadData uploadData) {
        return BeanCopyUtils.copyProperties(uploadData, UserDTO.class);
    }
}
