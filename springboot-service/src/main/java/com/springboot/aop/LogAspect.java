package com.springboot.aop;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.json.JSONUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基于注解实现
 * 使用 aop 切面记录请求日志信息
 * 执行顺序：环绕通知-->前置通知-->目标方法-->环绕通知-->后置通知-->返回通知
 */
//@Aspect
@Component
@Slf4j
public class LogAspect {
    private static final String START_TIME = "request-start";

    /**
     * 切入点
     */
    //@Pointcut("execution(public * com.springboot.controller.*Controller.*(..))")
    //public void pointcut() {
    //
    //}

    @Pointcut("@annotation(com.springboot.annotation.Pointcut)")
    public void pointcut() {

    }

    /**
     * 前置操作
     *
     * @param point 切入点
     */
    @Before("pointcut()")
    public void before(JoinPoint point) {
        log.info("前置通知");
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        //log.info("【请求 URL】：{}", request.getRequestURL());
        //log.info("【请求 IP】：{}", request.getRemoteAddr());
        //log.info("【请求类名】：{}，【请求方法名】：{}", point.getSignature().getDeclaringTypeName(), point.getSignature().getUsername());
        //
        //Map<String, String[]> parameterMap = request.getParameterMap();
        //log.info("【请求参数】：{}，", JSONUtil.toJsonStr(parameterMap));
        //Long start = System.currentTimeMillis();
        //request.setAttribute(START_TIME, start);
    }

    /**
     * 环绕操作
     * 围绕动态代理的全过程，需要携带ProceedingJoinPoint参数
     * 并且必须有返回值，且ProceedingJoinPoint类型的参数可以决定是否执行目标方法
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        log.info("环绕通知，执行之前");
        Object result = point.proceed();
        log.info("环绕通知，执行之后，返回值：{}", JSONUtil.toJsonStr(result));
        return result;
    }

    /**
     * 返回通知
     * 在目标方法正常结束之后执行
     */
    @AfterReturning(value = "pointcut()", returning = "returnValue")
    public void afterReturning(Object returnValue) {
        log.info("返回通知，返回值:{}", JSONUtil.toJsonStr(returnValue));
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();

        Long start = (Long)request.getAttribute(START_TIME);
        Long end = System.currentTimeMillis();
        //log.info("【请求耗时】：{}毫秒", end - start);

        String header = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(header);
        //	log.info("【浏览器类型】：{}，【操作系统】：{}，【原始User-Agent】：{}", userAgent.getBrowser().toString(), userAgent
		//	.getOperatingSystem().toString(), header);
    }

    /**
     * 后置通知
     * 在目标方法执行之后执行，与返回通知不同的是此通知无法拿到返回值
     * 并且不管是否发生异常都会执行
     */
    @After("pointcut()")
    public void after() {
        log.info("后置通知");
    }

    /**
     * 异常通知
     * 在目标方法非正常结束，发生异常或者抛出异常时执行
     *
     * @param e
     */
    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrowing(Throwable e) {
        log.error("异常通知e：{}", e.getMessage());
    }

}
