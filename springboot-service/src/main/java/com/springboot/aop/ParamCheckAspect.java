package com.springboot.aop;

import com.springboot.common.entity.Page;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ParamCheckAspect {

    @Pointcut("execution(public * com.springboot.controller..*.*(..,com.springboot.common.entity.Page,..))")
    public void paramCheckPointCut() {
    }

    @Before("paramCheckPointCut()")
    public void before(JoinPoint point) {
        Object[] args = point.getArgs();
        for(int i=0;i<args.length;i++){
            if(args[i] instanceof Page){
                Page page= (Page) args[i];
               if( page.getPageNum()==null||page.getPageSize()==null){
                    page.setPageNum(1);
                    page.setPageSize(20);
               }
            }
        }
    }
}
