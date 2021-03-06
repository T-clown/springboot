package com.springboot.handler;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import cn.hutool.json.JSONUtil;
import com.springboot.common.Result;
import com.springboot.common.ResultUtil;
import com.springboot.common.ServiceRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常拦截
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 实体参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handle(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("Entity Parameter error:{}", JSONUtil.toJsonStr(errors));
        // String errorMessage=e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultUtil.methodArgumentError(JSONUtil.toJsonStr(errors));
    }

    /**
     * 单个参数校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handle(ConstraintViolationException e) {
        log.error("Single Parameter error:{}", JSONUtil.toJsonStr(e.getMessage()));
        return ResultUtil.methodArgumentError(e.getMessage());
    }

    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceRuntimeException.class)
    public Result handle(ServiceRuntimeException e) {
        log.error("ServiceRuntimeException:{}", JSONUtil.toJsonStr(e.getMessage()));
        return ResultUtil.error(e.getResultCode(), e.getMessage());
    }

    /**
     * 未知异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result handle(Exception e) {
        log.error("Unknown error:{}", JSONUtil.toJsonStr(e.getMessage()));
        return ResultUtil.unknownError(e.getMessage());
    }
}
