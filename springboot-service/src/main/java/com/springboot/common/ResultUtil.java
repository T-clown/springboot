package com.springboot.common;

import com.springboot.enums.ResultCode;

public class ResultUtil {
    public ResultUtil() {
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return new Result(ResultCode.SUCCESS, data);
    }


    public static Result error(ResultCode errorCode) {
        return new Result(errorCode);
    }

    public static Result error(ResultCode code, String message) {
        return new Result(code, message);
    }

    public static Result methodArgumentError(String errorMessage) {
        return new Result(ResultCode.INVALID_PARAMETER, ResultCode.INVALID_PARAMETER.getMessage() + ":" + errorMessage);
    }

    public static Result unknownError(String message) {
        return new Result(ResultCode.UNKNOWN_ERROR, message);
    }
}
