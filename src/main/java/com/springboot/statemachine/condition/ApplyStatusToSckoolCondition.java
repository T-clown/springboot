package com.springboot.statemachine.condition;

import java.math.BigDecimal;

import com.springboot.dao.dto.UserDTO;
import com.springboot.statemachine.StateMachineContext;

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
