package com.springboot.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;

import com.springboot.dao.dto.UserDTO;
import com.springboot.entity.excel.UploadData;
import com.springboot.service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

// 有个很重要的点 ExcelUploadListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
public class ExcelUploadListener extends AnalysisEventListener<UploadData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUploadListener.class);
    private static final int BATCH_COUNT = 5;
    List<UserDTO> userDTOS = new ArrayList<>();

    /**
     * 当然如果不用存储这个对象没用。
     */
    private UserRepository userRepository;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     */
    public ExcelUploadListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 每一条数据解析都会来调用
     */
    @Override
    public void invoke(UploadData data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(data, userDTO);
        this.userDTOS.add(userDTO);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (this.userDTOS.size() >= BATCH_COUNT) {
            //持久化
            userRepository.addUser(userDTOS);
            // 存储完成清理 data
            this.userDTOS.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        //持久化
        userRepository.addUser(userDTOS);
        LOGGER.info("所有数据解析完成！");
    }

}
