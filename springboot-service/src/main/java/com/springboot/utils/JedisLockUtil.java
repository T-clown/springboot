package com.springboot.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
/**
 * https://github.com/redisson/redisson/wiki
 * 第一个为key，我们使用key来当锁，因为key是唯一的。
 * 第二个为value，我们传的是requestId，很多童鞋可能不明白，有key作为锁不就够了吗，为什么还要用到value？原因就是我们在上面讲到可靠性时，分布式锁要满足第四个条件解铃还须系铃人，通过给value
 * 赋值为requestId，我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。requestId可以使用UUID.randomUUID().toString()方法生成。
 * 第三个为nxxx，这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
 * 第四个为expx，这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
 * 第五个为time，与第四个参数相呼应，代表key的过期时间
 * 如果设置的过期时间比较短，而执行业务时间比较长，就会存在锁代码块失效的问题
 */
public class JedisLockUtil {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;
    /**
     * 通过lua方式加锁
     * 加锁脚本，KEYS[1] 要加锁的key，ARGV[1]是UUID随机值，ARGV[2]是过期时间
     */
    private static final String SCRIPT_LOCK
            = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('pexpire', KEYS[1], ARGV[2]) return 1 else "
            + "return 0 end";
    /**
     * 通过lua方式解锁
     * 解锁脚本，KEYS[1]要解锁的key，ARGV[1]是UUID随机值
     */
    private static final String SCRIPT_UNLOCK
            = "if redis.call('getUserById', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryLock(Jedis jedis, String lockKey, String requestId, long expireTime) {
        String result = jedis.set(lockKey, requestId,SetParams.setParams().nx().ex(expireTime).px(0L));
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param jedis     Redis客户端
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean release(Jedis jedis, String lockKey, String requestId) {
        String script
                = "if redis.call('getUserById', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

}
