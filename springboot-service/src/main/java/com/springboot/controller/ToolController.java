package com.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.springboot.common.entity.Result;
import com.springboot.common.utils.ResultUtil;
import com.springboot.controller.cycle.TestBeanA;
import com.springboot.common.delay.handler.DelayDTO;
import com.springboot.common.delay.RedisDelayKey;
import com.springboot.common.delay.RedisDelayQueue;
import com.springboot.domain.entity.Phone;
import com.springboot.common.extension.ImportAnnotation;
import com.springboot.common.extension.plugin.SmsRequest;
import com.springboot.common.extension.plugin.SmsService;
import com.springboot.common.extension.plugin.SmsType;
import com.springboot.handler.AsyncHandler;
import com.springboot.utils.RedisLockUtil;
import io.netty.util.concurrent.Promise;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Tag(name = "工具测试", description = "工具测试")
@RestController(value = "/tool")
@Slf4j
@Import({ImportAnnotation.ImportOne.class, ImportAnnotation.ImportTwoSelector.class, ImportAnnotation.ImportThreeRegistrar.class})
public class ToolController implements ApplicationContextAware {
    AtomicInteger counter = new AtomicInteger();

    @Autowired
    private RedisDelayQueue redisDelayQueue;

    @Autowired
    private Phone phone;

    @Autowired
    private SmsService smsService;


    @Operation(summary = "延迟任务")
    @PostMapping(value = "/delay/task")
    public Result<Void> delayTask(@RequestParam Integer count,@RequestParam Long delayTime ) {
        log.info(JSON.toJSONString(phone));
        log.info(Phone.cache);

        for (Integer i = 0; i < count; i++) {
            redisDelayQueue.addQueue(RedisDelayKey.DELAY_QUEUE_KEY, new DelayDTO(String.valueOf(counter.incrementAndGet())), delayTime, TimeUnit.SECONDS);
            //redisDelayQueue.addQueue(RedisDelayKey.DELAY_QUEUE_KEY2, new DelayDTO(String.valueOf(counter.incrementAndGet())), delayTime, TimeUnit.SECONDS);
            //redisDelayQueue.addQueue(RedisDelayKey.DELAY_QUEUE_KEY3, new DelayDTO(String.valueOf(counter.incrementAndGet())), delayTime, TimeUnit.SECONDS);
        }
        return ResultUtil.success();
    }

    @Operation(summary = "分布式锁")
    @PostMapping(value = "/redis/lock")
    public Result<Void> redisLock() {
       // boolean lock = RedisLockUtil.tryLock("distribute-lock", 0, 60);
        boolean lock = RedisLockUtil.tryLock("distribute-lock");
        log.info("线程[{}]获取分布式锁结果:{}", Thread.currentThread().getId(), lock);
        return ResultUtil.success();
    }

    @Autowired
    private CacheManager cacheManager;

    @Operation(summary = "缓存")
    @PostMapping(value = "/cache")
    public Result<String> cache() {
        Cache cache = cacheManager.getCache("cache");
        String value = null;
        if (cache != null) {
            value = cache.get("key", () -> {
                log.info("加载缓存");
                return "缓存";
            });
        }
        return ResultUtil.success(value);
    }

    @Operation(summary = "重定向")
    @PostMapping(value = "/redirect")
    public Result<String> redirect(HttpServletResponse response) {
        try {
            response.sendRedirect("/cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultUtil.success("");
    }

    @Operation(summary = "sms")
    @PostMapping(value = "/sms")
    public Result<Void> sms() {
        SmsRequest smsRequest = SmsRequest.builder().message("你好").smsType(SmsType.ALIYUN).build();
        smsService.sendSms(smsRequest);
        return ResultUtil.success();
    }

    @Autowired
    ImportAnnotation.ImportOne importOne;

    @Autowired(required = false)
    ImportAnnotation.ImportTwo importTwo;

    @Autowired(required = false)
    ImportAnnotation.ImportThree importThreeb;

    @Operation(summary = "import注解测试")
    @PostMapping(value = "/import/test")
    public Result<Void> importTest() {
        importOne.importOne();
        importTwo.importOne();
        importThreeb.importOne();
        return ResultUtil.success();
    }

//    @Lazy
//    @Autowired
//    private TestBeanA testBeanA;

    @Operation(summary = "循环依赖测试")
    @PostMapping(value = "/cycle/test")
    public Result<Void> cycle() {
        TestBeanA testBeanA = applicationContext.getBean(TestBeanA.class);
        testBeanA.test();
        return ResultUtil.success();
    }

    @Operation(summary = "dubbo远程调用")
    @PostMapping(value = "/dubbo/test")
    public Result<Void> dubbo() {
        return ResultUtil.success();
    }

    @Autowired
    private AsyncHandler asyncHandler;

    @Operation(summary = "异步调用测试")
    @PostMapping(value = "/async/test")
    public Result<Void> async() {
        asyncHandler.async(0);
        return ResultUtil.success();
    }


    @Operation(summary = "AOP日志")
    @PostMapping(value = "/aop/log")
    public Result<String> log(@RequestParam Long id) {
        String log = asyncHandler.log(id);
        return ResultUtil.success(log);
    }

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
