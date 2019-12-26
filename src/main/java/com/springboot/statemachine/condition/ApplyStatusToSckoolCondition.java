package com.springboot.statemachine.condition;

import java.math.BigDecimal;

import com.springboot.dao.dto.StudentDTO;
import com.springboot.statemachine.StateMachineContext;

public class ApplyStatusToSckoolCondition extends AbstractUntypedCondition {
    @Override
    public String toString() {
        return "新申请到上学中状态";
    }

    @Override
    public boolean isSatisfied(Object context) {
        StateMachineContext stateMachineContext = (StateMachineContext)context;
        StudentDTO studentDTO = stateMachineContext.getStudentDTO();
        BigDecimal currentMonthCount = stateMachineContext.getCurrentMonthCount();
        return studentDTO.getClassId() == 2 || currentMonthCount.compareTo(BigDecimal.ONE) < 1;
    }
}
