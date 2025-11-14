package com.example.kafka.consumer.presentation;

import com.example.kafka.consumer.application.SalesEventApplicationService;
import com.example.kafka.consumer.domain.entity.Sales;
import com.example.kafka.consumer.domain.entity.SalesLog;
import com.example.kafka.consumer.domain.entity.SalesItemSubtotal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Component
public class SalesLogConsumer {
    private final Logger logger = LoggerFactory.getLogger(SalesLogConsumer.class);
    private final SalesEventApplicationService salesEventApplicationService;
    private final ExecutorService executor;

    public SalesLogConsumer(
            SalesEventApplicationService salesEventApplicationService,
            ExecutorService executor
    ) {
        this.salesEventApplicationService = salesEventApplicationService;
        this.executor = executor;
    }

    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(@Payload SalesLog salesLog) {
        List<Sales> sales = salesLog.toSalesList();
        List<SalesItemSubtotal> salesItemSubtotal = salesLog.toSalesItemSubtotalList();

        executor.submit(() -> {
            try {
                salesEventApplicationService.register(sales, salesItemSubtotal);
            } catch (Exception e) {
                logger.error("Error processing sales for message: {}", salesLog, e);
            }
        });
    }
}
