package com.springboot.common;

import java.io.Serializable;

import com.springboot.enums.ResultCode;
import lombok.Data;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1696194043024336235L;

    /**
     * 响应码
     */
    private int code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public Result() {
    }

    public Result(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage(),null);
    }

    public Result(ResultCode resultCode, T data) {
        this(resultCode.getCode(), resultCode.getMessage(), data);
    }

    public Result(ResultCode code, String message) {
        this(code.getCode(), message, null);
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
