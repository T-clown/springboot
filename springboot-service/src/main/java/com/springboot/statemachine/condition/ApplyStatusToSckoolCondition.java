package com.springboot.statemachine.condition;

import com.springboot.domain.entity.UserDTO;
import com.springboot.statemachine.StateMachineContext;

import java.math.BigDecimal;

public class ApplyStatusToSckoolCondition extends AbstractUntypedCondition {
    @Override
    public String toString() {
        return "新申请到上学中状态";
    }

    @Override
    public boolean isSatisfied(Object context) {
        StateMachineContext stateMachineContext = (StateMachineContext)context;
        UserDTO userDTO = stateMachineContext.getUserDTO();
        BigDecimal currentMonthCount = stateMachineContext.getCurrentMonthCount();
        return  currentMonthCount.compareTo(BigDecimal.ONE) < 1;
    }
}
