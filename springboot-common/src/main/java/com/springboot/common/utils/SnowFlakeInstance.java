package com.springboot.common.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @description: 雪花算法单例
 */
public class SnowFlakeInstance extends Snowflake {
    /**
     * @param datacenterId data center number the process running on, value range: [0,31]
     * @param workerId     data center number the process running on, value range: [0,31]
     */
    public SnowFlakeInstance(long datacenterId, long workerId) {
        super(datacenterId, workerId);
    }

    private static volatile SnowFlakeInstance instance;

    public static SnowFlakeInstance getInstance() {
        if (instance == null) {
            synchronized (SnowFlakeInstance.class) {
                if (instance == null) {
                    instance = new SnowFlakeInstance(ThreadLocalRandom.current().nextInt(31), ThreadLocalRandom.current().nextInt(31));
                }
            }
        }
        return instance;
    }
}
