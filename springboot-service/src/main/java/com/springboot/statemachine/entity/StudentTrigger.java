package com.springboot.statemachine.entity;

import lombok.Getter;

@Getter
public enum StudentTrigger {
    /**
     *
     */
    ASSIGN_EVENT("自动分配"),
    EAT("吃饭"),
    ATTEND_CLASS("上课"),
    SLEEP("睡觉");

    private final String desc;

    StudentTrigger(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
