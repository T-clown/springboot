package com.springboot.common.utils;

import com.springboot.common.entity.Result;
import com.springboot.common.enums.ResultCode;

public class ResultUtil {
    public ResultUtil() {
    }

    public static Result<Void> success() {
        return new Result<>(ResultCode.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS, data);
    }


    public static Result<Void> error(ResultCode errorCode) {
        return new Result<>(errorCode);
    }

    public static Result<Void> error(ResultCode code, String message) {
        return new Result<>(code == null ? ResultCode.FAILURE : code, message);
    }

    public static Result<Void> methodArgumentError(String errorMessage) {
        return new Result<>(ResultCode.INVALID_PARAMETER, ResultCode.INVALID_PARAMETER.getMessage() + ":" + errorMessage);
    }

    public static Result<Void> unknownError(String message) {
        return new Result<>(ResultCode.UNKNOWN_ERROR, message);
    }
}
