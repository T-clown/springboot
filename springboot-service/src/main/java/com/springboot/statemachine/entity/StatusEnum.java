package com.springboot.statemachine.entity;


public enum StatusEnum {
    /**
     *
     */
    NEW_APPLICATION("NEW_APPLICATION", "新申请"),
    ATTEND_CLASS("ATTEND_CLASS", "上课"),
    RECESS("RECESS", "放假"),
    SUCCESS("SUCCESS", "成功"),
    FAIl("FAIl", "入学失败");

    String value;
    String desc;

    StatusEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static StatusEnum getEnum(String value) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.value.equals(value)) {
                return statusEnum;
            }
        }
        throw new RuntimeException( "无法解析:" + value + "对应的提现单状态");
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
