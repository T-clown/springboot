//package com.springboot.intercepter;
//
//public class RedisLock {
//
//
//    public boolean lock(long timeout,int expire){
//        long nanoTime = System.nanoTime();
//        timeout *= MILLI_NANO_TIME;
//        try {
//            //在timeout的时间范围内不断轮询锁
//            while (System.nanoTime() - nanoTime < timeout) {
//                //锁不存在的话，设置锁并设置锁过期时间，即加锁
//                if (this.redisClient.setnx(this.key, LOCKED) == 1) {
//                    this.redisClient.expire(key, expire);//设置锁过期时间是为了在没有释放
//                    //锁的情况下锁过期后消失，不会造成永久阻塞
//                    this.lock = true;
//                    return this.lock;
//                }
//                System.out.println("出现锁等待");
//                //短暂休眠，避免可能的活锁
//                Thread.sleep(3, RANDOM.nextInt(30));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("locking error",e);
//        }
//        return false;
//    }
//
//    public void unlock() {
//        try {
//            if(this.lock){
//                redisClient.delKey(key);//直接删除
//            }
//        } catch (Throwable e) {
//
//        }
//    }
//}
