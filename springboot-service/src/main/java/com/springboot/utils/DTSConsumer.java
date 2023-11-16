package com.springboot.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DTSConsumer {

    @Value("${mybatis.dts.env:dev}")
    private String env;

    private Set<String> dtsTables;

    private final JdbcTemplate jdbcTemplate;

    public DTSConsumer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Value("${mybatis.dts.subscribe:}")
    public void setDtsTables(String dtsTables) {
        String[] split = dtsTables.split(",");
        this.dtsTables = Arrays.stream(split).collect(Collectors.toSet());
    }

    //@KafkaListener(topics = "${spring.kafka.topics.dts:}", autoStartup = "${spring.kafka.consumer.switch:true}")
    void listener(String message) {
        DTSMessage dtsMessage = JSON.parseObject(message, DTSMessage.class);
        if (dtsMessage.getTableNames().stream().noneMatch(dtsTables::contains)) {
            return;
        }
        if (!Objects.equals(dtsMessage.getEnv(), env)) {
            return;
        }
        log.info("Receive dts message: {}", message);

        String sql = dtsMessage.getSql();
        //jdbcTemplate.execute(sql);
        log.info("Execute dts sql succeed: {}", message);
    }
}
