package com.springboot.util;

import com.springboot.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Slf4j
public class TemplateUtil {
    private static int LOCK_TIME=30;

    private static int LOCK_WAIT_TIME=0;
    /**
     * 加锁调用
     * @param callable
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T callLocked(Callable<T> callable, Object... params) {
        Assert.notEmpty(params,"加锁参数不能为空");
        String key = Arrays.stream(params).map(String::valueOf).collect(Collectors.joining(Constants.LINE));
        boolean lock = RedisLockUtil.tryLock(key, LOCK_WAIT_TIME, LOCK_TIME);
        T result=null;
        try {
            if(lock) {
                result = callable.call();
                log.info("result:{}", result);
            }
        } catch (Exception e) {
           e.printStackTrace();
        }finally {
            RedisLockUtil.unlock(key);
        }
        return result;
    }

    public static <T> T call(Callable<T> callable) {
        try {
            T response = callable.call();
            log.info("result:{}",response);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
