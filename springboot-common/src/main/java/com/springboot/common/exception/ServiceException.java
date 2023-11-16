package com.springboot.common.exception;

import com.springboot.common.enums.ResultCode;

public class ServiceException extends RuntimeException {
    ResultCode resultCode;
    String message;

    public ServiceException() {
    }

    public ServiceException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
        this.message = resultCode.getMessage();
    }

    public ServiceException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
        this.message = message;
    }

    public ServiceException(String message) {
        super(message);
        this.message = message;
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {

        super(cause);
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
