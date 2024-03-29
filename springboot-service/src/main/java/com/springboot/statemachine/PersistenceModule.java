package com.springboot.statemachine;

import com.springboot.common.DistributedLock;
import com.springboot.dao.dto.UserDTO;
import com.springboot.service.UserService;
import com.springboot.service.repository.UserRepository;
import com.springboot.statemachine.entity.StatusEnum;
import com.springboot.statemachine.entity.StudentTrigger;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.squirrelframework.foundation.exception.TransitionException;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.annotation.ListenerOrder;
import org.squirrelframework.foundation.fsm.annotation.OnTransitionBegin;
import org.squirrelframework.foundation.fsm.annotation.OnTransitionComplete;
import org.squirrelframework.foundation.fsm.annotation.OnTransitionEnd;
import org.squirrelframework.foundation.fsm.annotation.OnTransitionException;

/**
 * 持久化模块
 * 监听函数必须是public
 * 1.ListenerOrder 排序定义跨文件存在bug
 * 执行顺序，order越小越早得到执行，order缺省(未指定order)>已指定order
 * 2.AbstractStateMachine#addDeclarativeListener 函数必须是public的
 * // If no declarative listener was register, please make sure your listener was public method
 * 3.注意listener中出现异常都是会被捕获包装成SquirrelRuntimeException，但定义在StateMachine中的异常不会受这个限制
 */
@Service
public class PersistenceModule {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceModule.class);

    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DistributedLock distributedLock;


    /**
     * 先分布式锁
     */
    @OnTransitionBegin
    @ListenerOrder(0)
    public void tryLock(StatusEnum from, StudentTrigger event, StateMachineContext context) {
        UserDTO userDTO = context.getUserDTO();
        //创建分布式锁
        try {
            RLock rLock = distributedLock.lock(userDTO.getUsername());
            context.setRLock(rLock);
        } catch (Exception e) {
            logger.error("lock failed！id:{}", userDTO.getId());
            throw new RuntimeException("获取锁失败", e);
        }
    }

    /**
     * 优先开启事务
     */
    @OnTransitionBegin
    @ListenerOrder(1)
    public void getTransaction(StatusEnum from, StudentTrigger event, StateMachineContext context) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        context.setTransactionStatus(status);
    }

    /**
     * 优先提交事务
     */
    @OnTransitionComplete
    @ListenerOrder(1)
    public void commit(StatusEnum from, StatusEnum to, StudentTrigger event,
                       StateMachineContext context, UntypedStateMachine stateMachine) {
        try {
            //获取context信息
            UserDTO userDTO = context.getUserDTO();
            TransactionStatus transactionStatus = context.getTransactionStatus();
            //持久化
            userDTO.setStatus(to.getValue());
            //userService.updateUser(UserDTO, context.getOperator());
            userRepository.update(userDTO);
            //事务提交
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            logger.error("PersistenceModule.commit error", e);
            throw new RuntimeException("操作失败", e);
        }
    }

    /**
     * 事务提交后执行
     * 参数顺序不能变
     */
    @OnTransitionComplete
    @ListenerOrder(2)
    public void publishCompleteMsg(StatusEnum from, StatusEnum to,
                                   StateMachineContext context) {

        if (StatusEnum.SUCCESS.equals(to)) {
            UserDTO userDTO = context.getUserDTO();
            //发送短信
        }
    }

    /**
     * 优先回滚事务
     */
    @OnTransitionException
    @ListenerOrder(0)
    public void rollback(StatusEnum from, StatusEnum to, StudentTrigger event,
                         StateMachineContext context, TransitionException e) {
        TransactionStatus status = context.getTransactionStatus();
        if (status != null && !status.isCompleted()) {
            transactionManager.rollback(status);
        }
    }

    /**
     * 最后执行锁释放
     */
    @OnTransitionEnd
    @ListenerOrder(0)
    public void releaseLock(StatusEnum from, StatusEnum to, StateMachineContext context) {
        UserDTO UserDTO = context.getUserDTO();
        RLock rLock = context.getRLock();
        if (rLock != null) {
            try {
                rLock.unlock();
            } catch (Exception e) {
                logger.error("release lock failed！", e);
                throw new RuntimeException("释放锁失败", e);
            }
        }
    }

    @OnTransitionEnd
    @ListenerOrder(1)
    public void autoPaying(StatusEnum from, StatusEnum to,
                           StateMachineContext stateMachineContext,
                           UntypedStateMachine stateMachine) {
        if (to == StatusEnum.RECESS) {
            stateMachine.fire(StudentTrigger.SLEEP, stateMachineContext);
        }
    }

}
