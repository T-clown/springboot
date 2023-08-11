package com.springboot.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 过滤器
 */
@Slf4j
//@Component
//@Order(1)
//@WebFilter(urlPatterns = "/", filterName = "filterTest")
public class ContextFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("ContextFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("ContextFilter doFilter,filterUrl:{}", ((HttpServletRequest) servletRequest).getRequestURL().toString());
        filterChain.doFilter(servletRequest, servletResponse);
        log.info("ContextFilter doFilter,filterUrl:{}", ((HttpServletRequest) servletRequest).getRequestURL().toString());
    }

    @Override
    public void destroy() {
        log.info("ContextFilter destroy");
        Filter.super.destroy();
    }
}
