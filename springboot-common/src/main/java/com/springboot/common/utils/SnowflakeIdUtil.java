package com.springboot.common.utils;

/**
 *https://mp.weixin.qq.com/s/36iOXz2QDwH2mqETv15Mww
 * 高性能高可用：生成时不依赖于数据库，完全在内存中生成。
 * 容量大：每秒钟能生成数百万的自增 ID。
 * ID 自增：存入数据库中，索引效率高。
 *
 * 依赖与系统时间的一致性，如果系统时间被回调，或者改变，可能会造成 ID 冲突或者重复
 *
 * 可以改进改算法，将 10bit 的机器 ID 优化成业务表或者和我们系统相关的业务
 *
 * 符号位(1)+时间戳(41)+机房id(5)+机器id(5)+序列号(12)
 */
public class SnowflakeIdUtil {
    /**
     * 因为二进制里第一个 bit 为如果是 1，那么都是负数，但是我们生成的 id 都是正数，所以第一个 bit 统一都是 0
     */

    /**
     * 机器ID  2进制5位  32位减掉1位 31个
     */
    private final long workerId;
    /**
     * 机房ID 2进制5位  32位减掉1位 31个
     */
    private final long datacenterId;
    /**
     * 代表一毫秒内生成的多个id的最新序号  12位 4096 -1 = 4095 个
     * 每次生成一个id就递增，当毫秒数变化时重置
     */
    private long sequence;

    /**
     * 设置一个时间初始值 2^41 - 1 差不多可以用69年
     */
    private final long epoch = 1585644268888L;
    /**
     * 时间戳位数  2^41 - 1  69年
     */
    private final long timestampBits = 41L;
    /**
     * 5位的机器id
     */
    private final long workerIdBits = 5L;
    /**
     * 5位的机房id
     */
    private final long datacenterIdBits = 5L;
    /**
     * 每毫秒内产生的id数 2 的 12次方
     */
    private final long sequenceBits = 12L;

    /**
     * 这个是二进制运算，就是5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    /**
     * 同上，就是5 bit最多只能有31个数字，机房id最多只能是32以内
     */
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    private final long maxSequence = -1L ^ (-1L << sequenceBits);

    private final long workerIdShift = sequenceBits;
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**
     * 记录产生时间毫秒数，判断是否是同1毫秒
     */
    private long lastTimestamp = -1L;

    public long getWorkerId() {
        return workerId;
    }

    public long getDatacenterId() {
        return datacenterId;
    }

    public long getTimestamp() {
        return System.currentTimeMillis();
    }


    public SnowflakeIdUtil(long workerId, long datacenterId, long sequence) {

        // 检查机房id和机器id是否超过31 不能小于0
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }

        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException(
                    String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        this.sequence = sequence;
    }

    // 这个是核心方法，通过调用nextId()方法，让当前这台机器上的snowflake算法程序生成一个全局唯一的id
    public synchronized long nextId() {
        // 这儿就是获取当前时间戳，单位是毫秒
        long timestamp = timestampGen();
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 下面是说假设在同一个毫秒内，又发送了一个请求生成一个id
        // 这个时候就得把seqence序号给递增1，最多就是4096
        if (lastTimestamp == timestamp) {
            // 这个意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，
            //这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
            sequence = (sequence + 1) & maxSequence;
            //当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
            if (sequence == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            //不是同一毫秒内，重置序列号
            sequence = 0;
        }
        // 这儿记录一下最近一次生成id的时间戳，单位是毫秒
        lastTimestamp = timestamp;
        // 这儿就是最核心的二进制位运算操作，生成一个64bit的id
        // 先将当前时间戳左移，放到41 bit那儿；将机房id左移放到5 bit那儿；将机器id左移放到5 bit那儿；将序号放最后12 bit
        // 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        return ((timestamp - epoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) | sequence;
    }

    /**
     * 当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
     *
     * @param lastTimestamp
     * @return
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = timestampGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timestampGen();
        }
        return timestamp;
    }

    //获取当前时间戳
    private long timestampGen() {
        return System.currentTimeMillis();
    }

    /**
     * main 测试类
     *
     * @param args
     */
    public static void main(String[] args) {
//        IdWorker worker = new IdWorker(1, 1, 1);
//        for (int i = 0; i < 22; i++) {
//            System.out.println(worker.nextId());
//        }
    }
}
