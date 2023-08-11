package com.springboot.common.delay.handler;

import com.springboot.common.delay.BaseTask;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DelayDTO extends BaseTask {
    /**
     * 业务唯一属性id
     */
    private String identity;

    @Override
    public String taskIdentity() {
        return identity;
    }

}
