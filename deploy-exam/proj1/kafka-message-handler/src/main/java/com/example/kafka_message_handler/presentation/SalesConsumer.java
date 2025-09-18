package com.example.kafka_message_handler.presentation;

import com.example.kafka_message_handler.application.CustomerPointService;
import com.example.kafka_message_handler.domain.entity.Sales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SalesConsumer {
    private final Logger logger = LoggerFactory.getLogger(SalesConsumer.class);
    private final CustomerPointService customerPointService;

    public SalesConsumer(CustomerPointService customerPointService) {
        this.customerPointService = customerPointService;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.GROUP_ID) String groupId,
            @Header(KafkaHeaders.OFFSET) int offset,
            @Payload Sales sales
            ) {
        if (sales.customer() == null) {
            return;
        }
        try {
            customerPointService.calculateCustomerPoint(sales);
        } catch (Exception e) {
            //error 発生時にログを表示
            logger.error("Error processing message: Topic={}, Offset={}, Error={}",
                    topic, offset, e.getMessage(), e);
        }
    }
}
