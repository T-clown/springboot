package com.springboot.controller;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import com.springboot.aop.Pointcut;
import com.springboot.entity.Phone;
import com.springboot.entity.Yellow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Slf4j
public class PropertiesController {
    private static final String context = "你好，%s";
    private final AtomicInteger counter = new AtomicInteger();

    @Autowired
    private Yellow yellow;

    @Autowired
    private Phone phone;

    @RequestMapping(value = "/properties", method = GET)
    public Yellow properties(@RequestParam(value = "name", defaultValue = "美女") String name) {
        yellow.setId(counter.incrementAndGet());
        yellow.setName(String.format(context, name));
        return yellow;
    }

    @RequestMapping(value = "/phone", method = GET)
    @Pointcut
    public Phone phone() {
        log.info("目标方法：phone()");
        int a=2/0;
        phone.setId(counter.getAndIncrement());
        phone.setDateInProduced(new Date());
        return phone;
    }

}
