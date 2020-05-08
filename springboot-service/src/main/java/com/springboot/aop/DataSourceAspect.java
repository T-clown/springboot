package com.springboot.aop;

import java.lang.reflect.Method;

import com.springboot.annotation.DataSource;
import com.springboot.common.DynamicDataSourceContextHolder;
import com.springboot.common.DynamicRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("@annotation(com.springboot.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        DataSource dataSource = method.getAnnotation(DataSource.class);
        if (dataSource == null) {
            DynamicDataSourceContextHolder.setDataSourceKey("master");
        } else {
            String dataSourceName = dataSource.name();
            if (DynamicRoutingDataSource.isExistDataSource(dataSourceName) && !dataSourceName.equals(
                DynamicDataSourceContextHolder.getDataSourceKey())) {
                DynamicDataSourceContextHolder.setDataSourceKey(dataSourceName);
            }
        }
        try {
            return point.proceed();
        } finally {
            DynamicDataSourceContextHolder.clearDataSourceKey();
        }
    }
}
