package com.springboot.delay;

public class RedisDelayKey {
    /**
     * 业务队列key
     */
    private static final String DELAY_QUEUE_KEY = "DELAY_QUEUE";

    public static String getDelayQueueKey() {
        return DELAY_QUEUE_KEY;
    }

}
