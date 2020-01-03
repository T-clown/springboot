package com.springboot.statemachine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import com.springboot.dao.dto.UserDTO;
import com.springboot.service.UserService;
import com.springboot.statemachine.condition.ApplyStatusToSckoolCondition;
import com.springboot.statemachine.condition.SchoolStatusToHolidayStatusCondition;
import com.springboot.statemachine.converter.StatusConverter;
import com.springboot.statemachine.converter.TriggerConverter;
import com.springboot.statemachine.entity.StatusEnum;
import com.springboot.statemachine.entity.StudentTrigger;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.squirrelframework.foundation.component.SquirrelSingletonProvider;
import org.squirrelframework.foundation.exception.SquirrelRuntimeException;
import org.squirrelframework.foundation.exception.TransitionException;
import org.squirrelframework.foundation.fsm.ConverterProvider;
import org.squirrelframework.foundation.fsm.annotation.State;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.annotation.States;
import org.squirrelframework.foundation.fsm.annotation.Transit;
import org.squirrelframework.foundation.fsm.annotation.Transitions;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

@StateMachineParameters(stateType = StatusEnum.class, eventType = StudentTrigger.class,
    contextType = StateMachineContext.class)
@States({
    @State(name = "NEW_APPLICATION", initialState = true),
    @State(name = "FINANCIAL_AUDIT"),
    @State(name = "PAYING"),
    @State(name = "SUCCESS", isFinal = true, entryCallMethod = "paySuccess"),
    @State(name = "FAIl", isFinal = true, entryCallMethod = "payFailed")
})
@Transitions({
    @Transit(
        from = "NEW_APPLICATION",
        to = "FINANCIAL_AUDIT",
        on = "ASSIGN_EVENT",
        callMethod = "financialAuditStatus",
        when = SchoolStatusToHolidayStatusCondition.class
    ),
    @Transit(
        from = "NEW_APPLICATION",
        to = "PAYING",
        on = "ASSIGN_EVENT",
        when = ApplyStatusToSckoolCondition.class
    ),
    @Transit(
        from = "FINANCIAL_AUDIT",
        to = "PAYING",
        on = "FINANCE_APPROVED_EVENT"
    ),
    @Transit(
        from = "FINANCIAL_AUDIT",
        to = "FAIl",
        on = "FINANCE_UNAPPROVED_EVENT"
    ),
    @Transit(
        from = "PAYING",
        to = "SUCCESS",
        on = "AUTO_PAY_EVENT",
        callMethod = "pay"
    )
})
public class StudentStatusMachine extends AbstractUntypedStateMachine {
    static {
        ConverterProvider.INSTANCE.register(StatusEnum.class, new StatusConverter());
        ConverterProvider.INSTANCE.register(StudentTrigger.class, new TriggerConverter());
        ExecutorService autoRefundExecutorPool = new ThreadPoolExecutor(
            5, 5, 30, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            new BasicThreadFactory.Builder().namingPattern("autoRefundThread-%d").daemon(false).build(),
            new CallerRunsPolicy());
        SquirrelSingletonProvider.getInstance().register(ExecutorService.class, autoRefundExecutorPool);
        Runtime.getRuntime().addShutdownHook(new Thread(autoRefundExecutorPool::shutdown));
    }

    private static final Logger logger = LoggerFactory.getLogger(StudentStatusMachine.class);
    private UserService userService;

    public StudentStatusMachine(ApplicationContext applicationContext) {
        this.userService = applicationContext.getBean(UserService.class);
    }

    public void financialAuditStatus(StatusEnum fromState, StatusEnum toState,
                                     StudentTrigger event, StateMachineContext stateMachineContext)
        throws RuntimeException {
        //生成工单
        UserDTO UserDTO = stateMachineContext.getUserDTO();
        //其他业务操作,然后给UserDTO赋值
    }

    public void pay(StatusEnum fromState, StatusEnum toState,
                    StudentTrigger event, StateMachineContext stateMachineContext)
        throws RuntimeException {
        UserDTO UserDTO = stateMachineContext.getUserDTO();

    }

    public void payFailed(StatusEnum fromState, StatusEnum toState,
                          StudentTrigger event, StateMachineContext stateMachineContext) {
        UserDTO UserDTO = stateMachineContext.getUserDTO();
        //失败备注
        //UserDTO.setFailReason(stateMachineContext.getFailReason());
        //其他操作
    }

    public void paySuccess(StatusEnum fromState, StatusEnum toState,
                           StudentTrigger event, StateMachineContext stateMachineContext) {
        UserDTO UserDTO = stateMachineContext.getUserDTO();
        //其他操作
    }

    @Override
    protected void afterTransitionDeclined(Object fromState, Object event, Object context) {
        throw new RuntimeException("");
    }

    @Override
    protected void afterTransitionCausedException(Object fromState, Object toState, Object event, Object context) {
        throw getTargetException(getLastException());
    }

    private RuntimeException getTargetException(Throwable ex) {
        if (ex instanceof TransitionException) {
            Throwable targetException = ((TransitionException)ex).getTargetException();
            return getTargetException(targetException);
        } else if (ex instanceof SquirrelRuntimeException) {
            Throwable targetException = ((SquirrelRuntimeException)ex).getTargetException();
            return getTargetException(targetException);
        }else {
            logger.error("transit failed with unknown exception!", ex);
            return new RuntimeException("");
        }
    }
}
