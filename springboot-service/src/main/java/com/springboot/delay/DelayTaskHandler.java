package com.springboot.delay;

import com.alibaba.fastjson.JSON;
import com.springboot.entity.CreateUserRequest;
import com.springboot.service.CallBackService;
import com.springboot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class DelayTaskHandler {
    @Autowired
    private CallBackService callBackService;

    @Autowired
    private UserService userService;

    @Transactional(rollbackFor = Exception.class)
    public void handler(DelayDTO delayDTO){
        log.info("延迟任务处理:{}，线程:{}", JSON.toJSONString(delayDTO),Thread.currentThread().getName());
        CreateUserRequest request=new CreateUserRequest();
        request.setUsername("hahah");
        userService.add(request);
        callBackService.execute(()-> System.out.println("事务结束执行"));
    }


}
