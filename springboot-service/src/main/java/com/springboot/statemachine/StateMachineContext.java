package com.springboot.statemachine;

import java.math.BigDecimal;

import com.springboot.dao.dto.UserDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RLock;
import org.springframework.transaction.TransactionStatus;

@Data
public class StateMachineContext {
    private TransactionStatus transactionStatus;
    private RLock rLock;
    private UserDTO userDTO;
    private int version;
    private BigDecimal currentMonthCount;
    private String failReason;
    private Operator operator;

    @Getter
    public enum OperateRole {
        /**
         * 操作角色
         */
        TEACHER,
        STUDENT
    }

    @Getter
    @Setter
    public static class Operator {
        private OperateRole operateRole;
        private String operatorName;
        private int operatorId;
        private String note;
    }
}
