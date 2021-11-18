package com.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.springboot.service.EsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/es")
public class EsController {

    private final Map<String,EsService> map;

    public EsController(Map<String, EsService> map) {
        this.map = map;
    }


    @PostMapping("/addData")
    public void addData(@RequestParam("userId")String a) {
        System.out.println(JSON.toJSONString(map));

    }
}
