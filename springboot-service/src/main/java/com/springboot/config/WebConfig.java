package com.springboot.config;

import com.springboot.common.interceptor.ContextInterceptor;
import com.springboot.handler.PPageArgumentsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private PPageArgumentsResolver pageArgumentResolver;

    @Autowired(required = false)
    private ContextInterceptor contextInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(pageArgumentResolver);
    }

    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        //registry.addInterceptor(contextInterceptor).addPathPatterns("/**");
    }

    /**
     * 自定义资源映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
//        registry.addResourceHandler("/resource/**")
//                .addResourceLocations("file:E:/resource/");
    }

    /**
     * 全局序列化
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        // 设置Date类型字段序列化方式
//        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE));
//
//        // 指定BigDecimal类型字段使用自定义的序列化器
//        SimpleModule simpleModule = new SimpleModule();
//        simpleModule.addSerializer(BigDecimal.class, new BigDecimalSerializer());
//        objectMapper.registerModule(simpleModule);
//
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
//        converters.add(converter);
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    /**
     * 解决跨域
     * @param registry
     * https://mp.weixin.qq.com/s/7LZqwkXKC_Xi2XZlqv3qFw
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
    }


}
