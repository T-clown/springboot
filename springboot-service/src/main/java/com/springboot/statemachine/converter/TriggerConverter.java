package com.springboot.statemachine.converter;

import com.springboot.statemachine.entity.StudentTrigger;
import org.squirrelframework.foundation.fsm.Converter;


public class TriggerConverter implements Converter<StudentTrigger> {
    @Override
    public String convertToString(StudentTrigger trigger) {
        return trigger.name();
    }

    @Override
    public StudentTrigger convertFromString(String name) {
        return StudentTrigger.valueOf(name);
    }
}
