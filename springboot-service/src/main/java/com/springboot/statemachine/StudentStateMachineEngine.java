package com.springboot.statemachine;

import java.util.Objects;

import com.springboot.common.exception.ServiceRuntimeException;
import com.springboot.dao.dto.UserDTO;
import com.springboot.service.UserService;
import com.springboot.service.repository.UserRepository;
import com.springboot.statemachine.entity.StatusEnum;
import com.springboot.statemachine.entity.StudentTrigger;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.StateMachineConfiguration;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;

@Service
public class StudentStateMachineEngine implements ApplicationContextAware {
    protected UntypedStateMachineBuilder stateMachineBuilder;
    protected ApplicationContext applicationContext = null;
    @Autowired
    private UserRepository userService;

    public StudentStateMachineEngine() {
        stateMachineBuilder = StateMachineBuilderFactory.create(StudentStatusMachine.class, ApplicationContext.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public UserDTO fire(int id, StudentTrigger trigger, StateMachineContext context) {
        UserDTO UserDTO = userService.getById(id);
        if (Objects.isNull(UserDTO)) {
            throw new RuntimeException("无此学生");
        }
//        if (ObjectUtils.defaultIfNull(UserDTO.getVersion(), 0).intValue() != context.getVersion()) {
//            throw new RuntimeException("版本不对");
//        }
        context.setUserDTO(UserDTO);
        String status = UserDTO.getStatus();
        //注意：目前所有的状态机执行都是同步的，如果存在异步情况，注意此处的数据返回
        StudentStatusMachine stateMachine = stateMachineBuilder
            .newUntypedStateMachine(
                StatusEnum.getEnum(status),
                //暂时开启debug模式，进行日志trace
                StateMachineConfiguration.create().enableDebugMode(true).enableAutoStart(true),
                applicationContext);
        //持久化模块
        stateMachine.addDeclarativeListener(applicationContext.getBean(PersistenceModule.class));
        try {
            stateMachine.fire(trigger, context);
        } catch (Exception e) {
            throw new ServiceRuntimeException();
        }
        return UserDTO;
    }
}
