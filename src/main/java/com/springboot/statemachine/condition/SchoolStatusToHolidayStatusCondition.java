package com.springboot.statemachine.condition;

import java.math.BigDecimal;

import com.springboot.dao.dto.StudentDTO;
import com.springboot.statemachine.StateMachineContext;

public class SchoolStatusToHolidayStatusCondition extends AbstractUntypedCondition {
    @Override
    public String toString() {
        return "上学状态到请假状态";
    }

    @Override
    public boolean isSatisfied(Object context) {
        StateMachineContext stateMachineContext = (StateMachineContext)context;
        StudentDTO studentDTO = stateMachineContext.getStudentDTO();
        BigDecimal currentMonthCount = stateMachineContext.getCurrentMonthCount();
        return studentDTO.getClassId() == 1
            || currentMonthCount.compareTo(BigDecimal.ONE) > 0;
    }
}
