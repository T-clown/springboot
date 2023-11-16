package com.springboot.common.extension.plugin;


import org.springframework.plugin.core.Plugin;

/**
 * 依靠Spring的插件机制实现策略模式
 */
public interface SmsPlugin extends Plugin<SmsRequest> {

     SmsResponse<String> sendSms(SmsRequest smsRequest);


}
