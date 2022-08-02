package com.springboot.task;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.function.Consumer;


@Getter
@Setter
@Builder
public class Task {
    /**
     * 动态任务名曾
     */
    private String name;

    private String cronExpression;

    private Runnable runnable;

    /**
     * 设定动态任务开始时间
     */
    private LocalDateTime executeTime;
}

