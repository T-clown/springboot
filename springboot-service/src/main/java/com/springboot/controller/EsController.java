package com.springboot.controller;

import com.springboot.service.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/es")
public class EsController {

    @Autowired
    private EsService esService;


    @PostMapping("/addData")
    public void addData(@RequestParam("userId")String a) {
        System.out.println(a);

        esService.addData();
    }
}
