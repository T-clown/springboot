package com.springboot.controller.cycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
@Slf4j
@Lazy
@Component
public class TestBeanB {

    @Autowired
    private TestBeanA testBeanA;

    public void test(){
        log.info("TestBeanB.test");
    }
}
