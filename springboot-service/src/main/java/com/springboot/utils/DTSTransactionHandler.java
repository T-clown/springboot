package com.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

@Component
@Slf4j
public class DTSTransactionHandler implements TransactionSynchronization {

    private static KafkaTemplate<String, String> kafkaTemplate;

    private String dtsMessage;

    private String topic;

    public DTSTransactionHandler() {
    }

    public DTSTransactionHandler(String dtsMessage, String topic) {
        this.dtsMessage = dtsMessage;
        this.topic = topic;
    }

    @Override
    public void afterCompletion(int status) {
        if (status == TransactionSynchronization.STATUS_COMMITTED) {
            log.info( "Sending dts message: {}", dtsMessage);
            kafkaTemplate.send(topic, dtsMessage);
        } else {
            log.info( "Discard dts message: {}", dtsMessage);
        }
    }

    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        DTSTransactionHandler.kafkaTemplate = kafkaTemplate;
    }
}
