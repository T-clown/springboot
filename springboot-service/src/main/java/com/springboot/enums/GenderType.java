package com.springboot.enums;

import java.util.Arrays;

import com.springboot.common.ServiceRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderType {
    /**
     *
     */
    MALE("MALE", "男"),
    FEMALE("FEMALE", "男");

    String name;
    String desc;

    public static GenderType getEnum(String name) {
       return Arrays.stream(GenderType.values()).filter(x -> x.name.equals(name)).findAny().orElseThrow(
            () -> new ServiceRuntimeException(ResultCode.INVALID_PARAMETER, "无法解析" + name + "对应的性别"));
    }

}
