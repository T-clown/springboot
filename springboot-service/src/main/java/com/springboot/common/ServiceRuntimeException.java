package com.springboot.common;

import com.springboot.enums.ResultCode;

public class ServiceRuntimeException extends RuntimeException {
    ResultCode resultCode;
    String message;

    public ServiceRuntimeException() {}

    public ServiceRuntimeException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode=resultCode;
        this.message=resultCode.getMessage();
    }
    public ServiceRuntimeException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode=resultCode;
        this.message=message;
    }

    public ServiceRuntimeException(String message) {
        super(message);
    }

    public ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRuntimeException(Throwable cause) {

        super(cause);
    }

    public ResultCode getResultCode(){
        return resultCode;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
