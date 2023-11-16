package com.springboot.handler;

import com.springboot.common.aop.annotation.PrintLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class AsyncHandler {

    @Async("asyncExecutor")
    public void async(Integer value) {
        Integer var = 10;
        Integer a = var / value;
    }

    @PrintLog(description = "今天要下雨了")
    public String log(Long id){
        return id+"-"+"log";
    }
}
