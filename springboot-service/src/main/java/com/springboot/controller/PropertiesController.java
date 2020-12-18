package com.springboot.controller;

import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.json.JSONUtil;
import com.springboot.annotation.ImportSelector;
import com.springboot.annotation.RateLimiter;
import com.springboot.common.entity.Result;
import com.springboot.common.enums.CommonYN;
import com.springboot.common.util.ResultUtil;
import com.springboot.entity.Yellow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 同目录下properties比yml优先级高
 * 配置文件读取顺序：resources下的config目录文件-->resources下的配置文件
 * 读取properties中文会乱码，可转成yml文件解决
 */
@RestController
@Slf4j
@ImportSelector(mode = CommonYN.YES)
public class PropertiesController {
    private final AtomicInteger counter = new AtomicInteger();

    @Autowired
    private Yellow yellow;

    @Value("${p.name}")
    private String name;


    @RateLimiter(value = 0.5, timeout = 300)
    @GetMapping(value = "/yellow")
    public Result properties(@RequestParam(value = "name", defaultValue = "美女") String name) {
        log.info(JSONUtil.toJsonStr(yellow));
        yellow.setId(counter.incrementAndGet());
        return ResultUtil.success(yellow);
    }


}
