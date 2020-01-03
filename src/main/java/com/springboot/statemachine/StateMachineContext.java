package com.springboot.statemachine;

import java.math.BigDecimal;

import com.springboot.dao.dto.UserDTO;
import lombok.Data;
import org.springframework.transaction.TransactionStatus;

@Data
public class StateMachineContext {
    private TransactionStatus transactionStatus;
    //private JedisLock jedisLock;
    private UserDTO userDTO;
    private int version;
    private BigDecimal currentMonthCount;
    private String failReason;
    private Operator operator;

    public enum OperateRole {
        CUSTOMER,
        FINANCE
    }
    @Data
    public static class Operator {
        OperateRole operateRole;
        String operatorName;
        int operatorId;
        String note;
    }
}
