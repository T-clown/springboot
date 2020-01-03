package com.springboot.statemachine.entity;


/**
 * @author lbwwz
 */

public enum StatusEnum {
    /**
     *
     */
    NEW_APPLICATION(1, "新申请"),
    FINANCIAL_AUDIT(2, "待财务审核"),
    PAYING(3, "待打款"),
    SUCCESS(4, "提现成功"),
    FAIl(5, "提现失败");

    short value;
    String desc;

    StatusEnum(int value, String desc) {
        this.value = (short)value;
        this.desc = desc;
    }

    public static StatusEnum getEnum(short value) {
        for (StatusEnum statusEnum : StatusEnum.values()) {
            if (statusEnum.value == value) {
                return statusEnum;
            }
        }
        throw new RuntimeException( "无法解析:" + value + "对应的提现单状态");
    }

    public short getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
