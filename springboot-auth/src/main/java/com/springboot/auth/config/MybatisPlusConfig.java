package com.springboot.auth.config;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    @Bean
    public IdentifierGenerator idGenerator() {
        return new CustomIdGenerator();
    }

    /**
     * MybatisPlus自定义id生成器
     * 也可以直接在类上加上注解@Component，两种方式都是交给注入到Spring中
     */
    static class CustomIdGenerator implements IdentifierGenerator {

        @Override
        public boolean assignId(Object idValue) {
            return IdentifierGenerator.super.assignId(idValue);
        }

        @Override
        public Number nextId(Object entity) {
            return IdUtil.getSnowflakeNextId();
        }

        @Override
        public String nextUUID(Object entity) {
            return IdentifierGenerator.super.nextUUID(entity);
        }
    }

    /**
     * 1.尽量不要使用相对于System.getProperty（"user.dir"）当前用户目录的相对路径。这是一颗定时炸弹，随时可能要你的命。
     * 2.尽量使用URI形式的绝对路径资源。它可以很容易的转变为URI，URL，File对象。
     * 3.尽量使用相对classpath的相对路径。不要使用绝对路径。
     * 使用上面ClassLoaderUtil类的public static URL getExtendResource（String relativePath）方法已经能够使用相对于classpath的相对路径定位所有位置的资源。
     * 4.绝对不要使用硬编码的绝对路径。因为，我们完全可以使用ClassLoader类的getResource（""）方法得到当前classpath的绝对路径。
     * 如果你一定要指定一个绝对路径，那么使用配置文件，也比硬编码要好得多
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
        //得到的是当前类class文件的URI目录。不包括自己
        System.out.println(MybatisPlusConfig.class.getResource(""));
        //得到的是当前的classpath的绝对URI路径
        System.out.println(MybatisPlusConfig.class.getResource("/"));
        //得到的也是当前ClassPath的绝对URI路径
        System.out.println(MybatisPlusConfig.class.getClassLoader().getResource(""));
        //得到的也是当前ClassPath的绝对URI路径
        System.out.println(ClassLoader.getSystemResource(""));
        //得到的也是当前ClassPath的绝对URI路径
        System.out.println(Thread.currentThread().getContextClassLoader ().getResource(""));
    }
}
