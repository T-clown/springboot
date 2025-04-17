package com.springboot.common.aop;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Aspect
@Component
public class CommonMapperAspect {
    @Pointcut(value = "(execution(* com.springboot.mapper.CommonMapper.batchInsert(..)))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        List<?> list = (List<?>) point.getArgs()[0];
        setDefaultValue(list);
        return point.proceed();
    }

    private void setDefaultValue(List<?> list) throws IllegalAccessException {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Field[] fields = list.get(0).getClass().getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            for (Object obj : list) {
                Object value = field.get(obj);
                if (value == null) {
                    field.set(obj, getDefaultValue(field.getClass()));
                }
            }
        }
    }

    private Object getDefaultValue(Class<?> clazz) {
        if (String.class.equals(clazz)) {
            return StringUtils.EMPTY;
        }
        if (LocalDateTime.class.equals(clazz)) {
            return LocalDateTime.now();
        }
        if (Date.class.equals(clazz)) {
            return new Date();
        }
        return null;
    }
}
