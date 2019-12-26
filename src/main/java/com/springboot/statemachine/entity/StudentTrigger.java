package com.springboot.statemachine.entity;


public enum StudentTrigger {
    /**
     *
     */
    ASSIGN_EVENT("自动分配"),
    FINANCE_APPROVED_EVENT("财务审核通过"),
    FINANCE_UNAPPROVED_EVENT("财务审核未通过"),
    AUTO_PAY_EVENT("自动打款");

    private String desc;

    StudentTrigger(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
