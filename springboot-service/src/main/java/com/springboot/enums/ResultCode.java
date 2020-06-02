package com.springboot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
    /**
     * 接口调用成功
     */
    SUCCESS(0, "Request Successful"),

    /**
     * 服务器暂不可用，建议稍候重试。建议重试次数不超过3次。
     */
    FAILURE(-1, "System Busy"),
    INVALID_PARAMETER(-2, "参数错误"),
    UNKNOWN_ERROR(-3, "未知错误"),
    ES_ERROR(-4, "ES执行错误"),

    ;

    final int code;

    final String message;
}
