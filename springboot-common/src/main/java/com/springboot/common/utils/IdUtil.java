package com.springboot.common.utils;

public class IdUtil {
    public static Long generateId() {
        return SnowFlakeInstance.getInstance().nextId();
    }
}
