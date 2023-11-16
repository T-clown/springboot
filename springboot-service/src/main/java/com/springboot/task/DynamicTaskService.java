package com.springboot.task;

import cn.hutool.core.convert.ConverterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class DynamicTaskService {

    public Map<String, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();
    public List<String> tasks = new CopyOnWriteArrayList<>();


    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public DynamicTaskService(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    /**
     * 查看已开启但还未执行的动态任务
     * @return
     */
    public List<String> getTasks() {
        return tasks;
    }


    /**
     * 添加一个动态任务
     *
     * @param task
     * @return
     */
    public boolean add(Task task) {
        if (taskMap.containsKey(task.getName())) {
            stop(task.getName());
        }
        ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
        Date startTime = converterRegistry.convert(Date.class, task.getExecuteTime());
        log.info("添加定时任务:{}",task);
        //LocalDateTime of = LocalDateTime.of(LocalDate.now(), LocalTime.of(3, 0));
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(task.getRunnable(), startTime);
        taskMap.put(task.getName(), schedule);
        tasks.add(task.getName());
        return true;
    }


    public boolean add(Task task,String expression) {
        if (taskMap.containsKey(task.getName())) {
            stop(task.getName());
        }

        ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
        Date startTime = converterRegistry.convert(Date.class, task.getExecuteTime());

        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(task.getRunnable(), new CronTrigger(expression));
        taskMap.put(task.getName(), schedule);
        tasks.add(task.getName());
        return true;
    }


    /**
     * 停止任务
     */
    public boolean stop(String name) {
        if (taskMap.containsKey(name)) {
            return false;
        }
        ScheduledFuture<?> scheduledFuture = taskMap.get(name);
        scheduledFuture.cancel(true);
        taskMap.remove(name);
        tasks.remove(name);
        return true;
    }
}

