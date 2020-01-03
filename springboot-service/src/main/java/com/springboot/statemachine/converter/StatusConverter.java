package com.springboot.statemachine.converter;

import com.springboot.statemachine.entity.StatusEnum;
import org.squirrelframework.foundation.fsm.Converter;

public class StatusConverter implements Converter<StatusEnum> {
    @Override
    public String convertToString(StatusEnum statusEnum) {
        return statusEnum.name();
    }

    @Override
    public StatusEnum convertFromString(String name) {
        return StatusEnum.valueOf(name);
    }
}
