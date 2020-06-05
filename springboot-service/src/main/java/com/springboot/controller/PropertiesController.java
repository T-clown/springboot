package com.springboot.controller;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import cn.hutool.json.JSONUtil;
import com.springboot.annotation.Pointcut;
import com.springboot.annotation.RateLimiter;
import com.springboot.entity.Phone;
import com.springboot.entity.Yellow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * properties比yml优先级高
 * 配置文件读取顺序：resources下的config目录文件-->resources下的配置文件
 */
@RestController
@Slf4j
public class PropertiesController {
    private static final String context = "你好，%s";
    private final AtomicInteger counter = new AtomicInteger();

    @Autowired
    private Yellow yellow;
    @Autowired
    private Phone phone;

    @RateLimiter(value = 0.5, timeout = 300)
    @GetMapping(value = "/yellow")
    public Yellow properties(@RequestParam(value = "username", defaultValue = "美女") String name) {
        log.info(JSONUtil.toJsonStr(yellow));
        yellow.setId(counter.incrementAndGet());
        yellow.setName(String.format(context, name));
        return yellow;
    }

    @GetMapping(value = "/phone")
    @Pointcut
    public Phone phone() {
        log.info(JSONUtil.toJsonStr(phone));
        log.info("目标方法：phone()");
        //int a=2/0;
        phone.setId(counter.getAndIncrement());
        phone.setDateInProduced(new Date());
        return phone;
    }

}
