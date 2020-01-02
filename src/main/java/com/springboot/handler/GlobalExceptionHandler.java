package com.springboot.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.ConstraintViolationException;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.springboot.common.Result;
import com.springboot.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.error("参数错误:{}", JSONUtil.toJsonStr(errors));
        // String errorMessage=e.getBindingResult().getFieldError().getDefaultMessage();
        return ResultUtil.methodArgumentError(JSONUtil.toJsonStr(errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleConstraintViolationException(ConstraintViolationException e) {
        return ResultUtil.methodArgumentError(e.getMessage());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public Dict handler(RuntimeException ex) {
        return Dict.create().set("message", ex.getMessage());
    }
}
