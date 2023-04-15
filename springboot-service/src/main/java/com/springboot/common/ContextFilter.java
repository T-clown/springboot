package com.springboot.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@Order(1)
@WebFilter(urlPatterns = "/", filterName = "filterTest")
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
