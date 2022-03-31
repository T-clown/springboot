package com.springboot.config;

import com.springboot.aop.Advice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于接口实现aop配置
 */
@Configuration
public class InterfaceAopConfig {

    public static final String execution = "@annotation(com.springboot.annotation.Pointcut)";

    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor2() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(execution);
        // 配置增强类advisor
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(new Advice());
        return advisor;
    }

}
