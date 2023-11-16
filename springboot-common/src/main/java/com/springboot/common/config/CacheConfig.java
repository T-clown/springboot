package com.springboot.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * https://segmentfault.com/a/1190000038665523
 */
@Slf4j
@EnableCaching
@Configuration
public class CacheConfig {

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */
    @Bean
    public CacheManager cacheManager() {

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 访问后60秒过期
                //.expireAfterAccess(60, TimeUnit.SECONDS)
                //写入后60秒过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                //.softValues()
                //.weakKeys()
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000));
                //.recordStats());
        return cacheManager;
    }

}
