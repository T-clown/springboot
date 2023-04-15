package com.springboot.controller;

import cn.hutool.aop.proxy.ProxyFactory;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.alibaba.fastjson.JSON;
import com.mysql.cj.jdbc.Driver;
import com.springboot.common.entity.Result;
import com.springboot.common.util.ResultUtil;
import com.springboot.delay.DelayDTO;
import com.springboot.delay.RedisDelayKey;
import com.springboot.delay.RedisDelayQueue;
import com.springboot.entity.Phone;
import com.springboot.util.RedisLockUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Api(tags = {"工具测试"})
@RestController(value = "/tool")
@Slf4j
public class ToolController {
    AtomicInteger counter = new AtomicInteger();

    @Autowired
    private RedisDelayQueue redisDelayQueue;

    @Autowired
    private Phone phone;

    @ApiOperation("延迟任务")
    @PostMapping(value = "/delay/task")
    public Result<Void> delayTask() {
        log.info(JSON.toJSONString(phone));
        log.info(Phone.cache);
        redisDelayQueue.addQueue(RedisDelayKey.getDelayQueueKey(), new DelayDTO(String.valueOf(counter.incrementAndGet())), 6, TimeUnit.SECONDS);
        return ResultUtil.success();
    }

    @ApiOperation("分布式锁")
    @PostMapping(value = "/redis/lock")
    public Result<Void> redisLock() throws InterruptedException {
        boolean lock = RedisLockUtil.tryLock("lock", 0, 60);
        log.info("线程[{}]获取分布式锁结果:{}", Thread.currentThread().getId(), lock);
        return ResultUtil.success();
    }

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    private static final ThreadLocal<String> THREAD_LOCAL = ThreadLocal.withInitial(() -> "ThreadLocal初始值");

    @ApiOperation("测试")
    @PostMapping(value = "/test")
    public Result<Void> redisLock(boolean gc) throws InterruptedException {
        threadPoolExecutor.setCorePoolSize(1000);
        threadPoolExecutor.execute(() -> test(gc));
        return ResultUtil.success();
    }

    private void test(boolean gc) {
        try {
            THREAD_LOCAL.set(Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(30);
            if (gc) {
                System.gc();
                Runtime.getRuntime().gc();
            }
            Thread t = Thread.currentThread();
            Class<? extends Thread> clz = t.getClass();
            Field field = clz.getDeclaredField("threadLocals");
            field.setAccessible(true);
            Object threadLocalMap = field.get(t);
            Class<?> tlmClass = threadLocalMap.getClass();
            Field tableField = tlmClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] arr = (Object[]) tableField.get(threadLocalMap);
            for (Object o : arr) {
                if (o != null) {
                    Class<?> entryClass = o.getClass();
                    Field valueField = entryClass.getDeclaredField("value");
                    Field referenceField = entryClass.getSuperclass().getSuperclass().getDeclaredField("referent");
                    valueField.setAccessible(true);
                    referenceField.setAccessible(true);
                    log.info("弱引用key:[{}],值:[{}]", referenceField.get(o), valueField.get(o));
                }
            }
        } catch (Exception e) {

        }
    }

}
