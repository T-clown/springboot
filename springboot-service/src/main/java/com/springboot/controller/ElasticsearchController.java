package com.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.springboot.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@Slf4j
@RestController("/elasticsearch")
public class ElasticsearchController {

    private final Map<String, ElasticsearchService> map;

    public ElasticsearchController(Map<String, ElasticsearchService> map) {
        this.map = map;
    }


    @PostMapping("/addData")
    public void addData(@RequestParam("userId")String a) {
        log.info(JSON.toJSONString(map));

    }
}
