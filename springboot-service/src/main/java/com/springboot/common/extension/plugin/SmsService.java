package com.springboot.common.extension.plugin;


import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.SerializationException;
import org.springframework.plugin.core.PluginRegistry;

import java.util.Optional;


@RequiredArgsConstructor
public class SmsService {


    private final PluginRegistry<SmsPlugin, SmsRequest> pluginRegistry;


    public SmsResponse<String> sendSms(SmsRequest smsRequest) {
        Optional<SmsPlugin> smsPlugin = pluginRegistry.getPluginFor(smsRequest);
        return smsPlugin.orElseThrow(() -> new SerializationException("Sms plugin is not binder with type : 【" + smsRequest.getSmsType() + "】"))
                .sendSms(smsRequest);


    }
}
