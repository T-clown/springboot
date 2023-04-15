package com.springboot.statemachine.demo;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.squirrelframework.foundation.fsm.StateMachineBuilderFactory;
import org.squirrelframework.foundation.fsm.UntypedStateMachine;
import org.squirrelframework.foundation.fsm.UntypedStateMachineBuilder;
import org.squirrelframework.foundation.fsm.annotation.StateMachineParameters;
import org.squirrelframework.foundation.fsm.impl.AbstractUntypedStateMachine;

/**
 * http://hekailiang.github.io/squirrel/
 */
@Slf4j
public class QuickStartSample {
    //定义状态机各个状态
    enum State {
        /**
         *
         */
        A, B, C, D
    }

    // 定义状态机触发事件
    enum StateEvent {
        /**
         *
         */
        ToA, ToB, ToC, ToD
    }


    // 2. Define State Machine Class
    @StateMachineParameters(stateType=String.class, eventType=StateEvent.class, contextType=Integer.class)
    static class StateMachineSample extends AbstractUntypedStateMachine {
        protected void fromAToB(String from, String to, StateEvent event, Integer context) {
            log.info("Transition from '"+from+"' to '"+to+"' on event '"+event+
                "' with context '"+context+"'.");
        }

        protected void ontoB(String from, String to, StateEvent event, Integer context) {
            log.info("Entry State \'"+to+"\'.");
        }
    }

    public static void main(String[] args) {
        // 3. Build State Transitions
        UntypedStateMachineBuilder builder = StateMachineBuilderFactory.create(StateMachineSample.class);
        builder.externalTransition().from("A").to("B").on(StateEvent.ToB).callMethod("fromAToB");
        builder.onEntry("B").callMethod("ontoB");

        // 4. Use State Machine
        UntypedStateMachine fsm = builder.newStateMachine("A");
        fsm.fire(StateEvent.ToB, 10);

        log.info("Current state is "+fsm.getCurrentState());
    }
}
