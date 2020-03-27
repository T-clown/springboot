package com.springboot.enums;

import java.util.Arrays;

public enum CommonYN {
    /**
     *是否
     */
    YES(1, "是"),

    NO(0, "否");

    short value;
    String desc;

    CommonYN(int value, String desc) {
        this.value = (short)value;
        this.desc = desc;
    }

    public static CommonYN getEnum(int value) {
        CommonYN[] types = CommonYN.values();
        return Arrays.stream(types)
            .filter(type -> type.value() == value)
            .findAny().orElse(null);
    }

    public short value() { return value; }

    public String getDesc() {
        return desc;
    }

    public boolean equalTo(short type) {
        return this.value == type;
    }

    public boolean equalTo(int type) {
        return this.value == type;
    }
}
