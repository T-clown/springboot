package com.springboot.controller.cycle;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
@Slf4j
@Lazy
@Component
public class TestBeanA {

    @Autowired
    private TestBeanB testBeanB;

    public void test(){
        log.info("TestBeanA.test");
        testBeanB.test();
    }
}
