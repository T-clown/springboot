package com.springboot.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
public class RedisDataEntity<T> implements Serializable {

    /**
     * 数据
     */
    private T data;
    /**
     * 过期时间
     */
    private Long delay;
    /**
     * 任务名称
     */
    private String queueName;
    /**
     * 添加时间
     */
    private Date addTime = new Date();

    /**
     * 过期时间单位
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    public RedisDataEntity(T data, Long delay, TimeUnit timeUnit, String queueName) {
        this.data = data;
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.queueName = queueName;
    }

    public RedisDataEntity(T data, Long delay, String queueName) {
        this.data = data;
        this.delay = delay;
        this.queueName = queueName;
    }

    @Override
    public String toString() {
        return "{延迟任务名称:" + queueName +
                "data:" + JSON.toJSONString(data) +
                ", 延迟时间:" + delay + " " +
                timeUnit.toString() +
                ", 添加时间:'" + addTime + '\'' +
                '}';
    }
}

