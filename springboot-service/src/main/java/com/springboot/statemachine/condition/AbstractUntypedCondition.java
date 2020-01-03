package com.springboot.statemachine.condition;

import org.squirrelframework.foundation.fsm.UntypedCondition;

/**
 * 抽象无类型约束条件
 */
public abstract class AbstractUntypedCondition implements UntypedCondition {
    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    /**
     * 条件说明
     *
     * @return 条件说明
     */
    @Override
    public abstract String toString();
}
