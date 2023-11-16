package com.springboot.handler;

import com.alibaba.fastjson.JSON;
import com.springboot.common.entity.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class PPageArgumentsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 一个api接口，多个参数，将会多次调用此方法匹配是不是你需要的参数类型
        Class<?> clazz = methodParameter.getParameterType();
        ///log.info(JSON.toJSONString(methodParameter));
        log.info("===============【参数装配】clazz={}",clazz.getName());
        //return Page.class.getName().equalsIgnoreCase(clazz.getName());
        return false;
    }

    /**
     *  该方法返回就是Controller中api方法中参数赋的值
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return new Page();
    }
}
