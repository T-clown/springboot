package com.springboot.common.delay.handler;

import com.alibaba.fastjson.JSON;
import com.springboot.common.delay.handler.DelayDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DelayTaskHandler {

    public void handler(DelayDTO delayDTO){
        log.info("延迟任务处理:{}，线程:{}", JSON.toJSONString(delayDTO),Thread.currentThread().getName());
    }

}
