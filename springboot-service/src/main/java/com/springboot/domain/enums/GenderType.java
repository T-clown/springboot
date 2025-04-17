package com.springboot.domain.enums;

import cn.hutool.core.lang.Assert;
import com.springboot.common.enums.ResultCode;
import com.springboot.common.exception.ServiceException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum GenderType {
    /**
     *
     */
    MALE("MALE", "男"),
    FEMALE("FEMALE", "女");

    final String name;
    final String desc;

    static final Map<String, GenderType> ENUM_MAP;

    static {
        ENUM_MAP = Arrays.stream(GenderType.values()).collect(Collectors.toMap(x -> x.name, Function.identity()));
    }

    public static GenderType getEnum2(String name) {
        GenderType genderType = ENUM_MAP.get(name);
        Assert.notNull(genderType, () -> new ServiceException(ResultCode.INVALID_PARAMETER, "无法解析" + name + "对应的性别"));
        return genderType;
    }

    public static GenderType getEnum(String name) {
        return Arrays.stream(GenderType.values()).filter(x -> x.name.equals(name)).findAny().orElseThrow(
                () -> new ServiceException(ResultCode.INVALID_PARAMETER, "无法解析" + name + "对应的性别"));
    }

}
