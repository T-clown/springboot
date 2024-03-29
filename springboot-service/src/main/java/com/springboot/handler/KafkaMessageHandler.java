package com.springboot.handler;

import com.springboot.common.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * 消息处理器
 */
@Component
@Slf4j
public class KafkaMessageHandler {

    @KafkaListener(topics = Constants.KAFKA_TOPIC_NAME, containerFactory = "ackContainerFactory")
    public void handleMessage(ConsumerRecord<String,String> record, Acknowledgment acknowledgment) {
        try {
            String message = record.value();
            log.info("收到消息: {}", message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 手动提交 offset
            acknowledgment.acknowledge();
        }
    }
}
