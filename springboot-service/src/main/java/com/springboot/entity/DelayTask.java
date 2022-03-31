package com.springboot.entity;

import com.springboot.util.LocalDateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayTask implements Delayed {
    private String name;
    private long start = System.currentTimeMillis();
    //minute
    private long delayTime;

    public DelayTask(String name, long delayTime) {
        this.name = name;
        this.delayTime = delayTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert((start + TimeUnit.MINUTES.toMillis(delayTime) - System.currentTimeMillis()), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "DelayeTask[ " +
                "name='" + name + '\'' +
                ", delayTime=" + delayTime + " m,任务加入时间:" + LocalDateTimeUtil.format(new Date(start)) + ",执行时间" + LocalDateTimeUtil.format(LocalDateTime.now()) +
                ']';
    }
}
