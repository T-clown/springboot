package com.springboot.common;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;

public interface DistributedLock {
    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

    RCountDownLatch getCountDownLatch(String name);

    RSemaphore getSemaphore(String name);
}
