package com.springboot.common.extension.plugin;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliyunSmsPlugin implements SmsPlugin {
    @Override
    public  SmsResponse<String> sendSms(SmsRequest smsRequest) {
        log.info("来自阿里云短信：{}" , JSON.toJSONString(smsRequest));
        return SmsResponse.<String>builder()
                .code("200").message("发送成功")
                .success(true).result("阿里云短信的回执").build();
    }

    @Override
    public boolean supports(SmsRequest smsRequest) {
        return SmsType.ALIYUN == smsRequest.getSmsType();
    }
}
