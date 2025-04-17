package com.springboot.controller;

import com.alibaba.fastjson2.JSON;
import com.springboot.common.aop.annotation.ImportSelector;
import com.springboot.common.entity.Result;
import com.springboot.common.enums.CommonYN;
import com.springboot.common.utils.ResultUtil;
import com.springboot.domain.entity.Yellow;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 同目录下properties比yml优先级高
 * 配置文件读取顺序：resources下的config目录文件>resources下的配置文件
 * 读取properties中文会乱码，可转成yml文件解决
 * <p>
 * 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
 *
 * @import注解主要作用是将类的实例加入到IOC容器中
 */
@RestController
@Slf4j
@RequestMapping("/properties")
@ImportSelector(mode = CommonYN.YES)
//@Import(UserServiceImpl.class)
public class PropertiesController {
    private final AtomicInteger counter = new AtomicInteger();

    @Autowired
    private Yellow yellow;

    @Value("${p.name}")
    private String name;

    @Value("${p.cachename}")
    private String cacheName;

    @Value("${test.value}")
    private Set<String> valueSet;
    @Value("${test.value}")
    private List<String> valueList;
    @Value("${test.value}")
    private String[] valueArray;
    @Value("${test.value}")
    private String value;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    //@TokenRateLimiter(qps = 0.5)
    @GetMapping(value = "/yellow")
    public Result<Yellow> properties(@RequestParam(value = "name", defaultValue = "美女") String name) {
        yellow.setName("访问8083端口");
        yellow.setId(counter.incrementAndGet());
        return ResultUtil.success(yellow);
    }

    @PostMapping("/test")
    public Result<Void> test(@RequestBody List<Long> ids) {
        log.info(value);
        log.info(JSON.toJSONString(valueArray));
        log.info(JSON.toJSONString(valueList));
        log.info(JSON.toJSONString(valueSet));
        log.info(JSON.toJSONString(ids));
        return ResultUtil.success();
    }

    @PostMapping("/send")
    public Result<Void> send(@RequestParam("message") String message) {
        log.info("参数：{}", message);
//        CompletableFuture<SendResult<String, String>> sendResult = kafkaTemplate.send(Constants.KAFKA_TOPIC_NAME, message);
//        sendResult.whenComplete((i, t) -> {
//            log.info("i:{},t:{}", i, t);
//        });
        return ResultUtil.success();
    }

    @PostMapping("/send2")
    public Result<Void> send(@RequestBody Message message) {
        log.info("参数：{}", JSON.toJSONString(message));
        return ResultUtil.success();
    }

    @Getter
    @Setter
    static class Message {
        private String message;
    }


}
