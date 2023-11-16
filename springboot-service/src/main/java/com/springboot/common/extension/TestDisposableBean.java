package com.springboot.common.extension;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 其触发时机为当此对象销毁时，会自动执行这个方法。
 * 比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。
 */
@Slf4j
@Component
public class TestDisposableBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        log.error("[DisposableBean] destroy");
    }

    @PreDestroy
    public void preDestroy(){
        log.error("[DisposableBean] preDestroy");
    }

}
