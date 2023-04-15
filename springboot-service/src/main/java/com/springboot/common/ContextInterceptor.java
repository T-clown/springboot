package com.springboot.common;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Interceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Spring 框架支持的AOP
 * 日志记录 ：几率请求信息的日志，以便进行信息监控、信息统计等等
 * 权限检查 ：对用户的访问权限，认证，或授权等进行检查
 * 性能监控 ：通过拦截器在进入处理器前后分别记录开始时间和结束时间，从而得到请求的处理时间
 * 通用行为 ：读取 cookie 得到用户信息并将用户对象放入请求头中，从而方便后续流程使用
 * <p>
 * 只有经过 DispatcherServlet 的请求才会被拦截器捕获，而自定义的 Servlet 请求则不会被拦截的
 */
@Slf4j
@Component
public class ContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("ContextInterceptor preHandle,method:{}", request.getRequestURL().toString());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        log.info("ContextInterceptor postHandle,method:{}", request.getRequestURL().toString());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        log.info("ContextInterceptor afterCompletion,method:{}", request.getRequestURL().toString());
    }
}
