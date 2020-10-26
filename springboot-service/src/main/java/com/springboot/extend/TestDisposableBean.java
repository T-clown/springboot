package com.springboot.extend;

import org.springframework.beans.factory.DisposableBean;

/**
 * 其触发时机为当此对象销毁时，会自动执行这个方法。
 * 比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。
 */
public class TestDisposableBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {
        System.out.println("[DisposableBean] TestDisposableBean");
    }

}
