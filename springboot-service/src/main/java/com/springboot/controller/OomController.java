package com.springboot.controller;

import com.springboot.common.entity.Result;
import com.springboot.common.utils.ResultUtil;
import com.springboot.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Tag(name = "模拟OOM")
@Slf4j
@RestController
@RequestMapping("/oom")
public class OomController {

    /**
     * java.lang.OutOfMemoryError: Java heap space
     *
     * @param count
     * @return
     */
    @Operation(summary = "大对象")
    @PostMapping("/big/object")
    public Result<Void> bigObject(@RequestParam Long count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(new User());
        }
        return ResultUtil.success();
    }

    /**
     * 会阻塞，堆消耗曲线呈现毛刺状，系统假死，系统不可用
     *
     * @param count
     * @return
     */
    @Operation(summary = "创建大数组")
    @PostMapping("/big/array")
    public Result<Void> bigArray(@RequestParam Long count) {
        Object[] objects = new Object[count.intValue()];
        return ResultUtil.success();
    }


    /**
     * java.lang.OutOfMemoryError: unable to create native thread: possibly out of memory or process/resource limits reached
     * 没有堆文件产生
     *
     * @param count
     * @return
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Operation(summary = "创建许多线程")
    @PostMapping("/thread")
    public Result<Void> thread(@RequestParam Long count) {
        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                log.info("创建线程[{}]", Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {

                }
            }, "线程-" + i).start();
        }
        return ResultUtil.success();
    }


    private static final ThreadLocal<User> local = new ThreadLocal<>();

    /**
     * @param count
     * @return
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    @Operation(summary = "ThreadLocal内存泄漏")
    @PostMapping("/threadlocal")
    public Result<Void> threadLocal(@RequestParam Long count) {

        for (int i = 0; i < count; i++) {
            new Thread(() -> {
                local.set(new User());
            }).start();
        }
        return ResultUtil.success();
    }

    private static final List<User> list = new ArrayList<>();

    /**
     * java.lang.OutOfMemoryError: Java heap space
     * 系统会一直执行FGC 系统不可用
     *
     * @param count
     * @return
     */
    @Operation(summary = "静态变量")
    @PostMapping("/static")
    public Result<Void> staticValue(@RequestParam Long count) {

        for (int i = 0; i < count; i++) {
            list.add(new User());
        }
        return ResultUtil.success();
    }


}
