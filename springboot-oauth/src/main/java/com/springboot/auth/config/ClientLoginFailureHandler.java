package com.springboot.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 登录失败处理器，失败后携带失败信息重定向到登录地址重新登录.
 */
@Slf4j
@Component
public class ClientLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.debug("Login failed!");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.sendRedirect("/oauth/login?error=" + URLEncoder.encode(exception.getLocalizedMessage(), StandardCharsets.UTF_8));
    }
}
