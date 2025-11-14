package com.example.kafka.consumer.presentation;

import com.example.kafka.consumer.application.SalesEventApplicationService;
import com.example.kafka.consumer.config.TestcontainersConfiguration;
import com.example.kafka.consumer.domain.entity.Sales;
import com.example.kafka.consumer.domain.entity.SalesLog;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@DirtiesContext
@Import(TestcontainersConfiguration.class)
class SalesLogConsumerTest {
    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.producer.value-serializer", () -> "org.springframework.kafka.support.serializer.JsonSerializer");
        registry.add("spring.kafka.consumer.value-deserializer", () -> "org.springframework.kafka.support.serializer.JsonDeserializer");
    }

    @MockitoBean
    private SalesEventApplicationService salesEventApplicationService;

    @Autowired
    private KafkaOperations<String, SalesLog> kafkakafkaOperations;

    @Test
    void testListenKafkaMessage() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Mockito.doAnswer(invocationOnMock -> {
            latch.countDown();
            return null;
        }).when(salesEventApplicationService).register(Mockito.any(),  Mockito.any());

        ArgumentCaptor<List<Sales>> salesCaptor = ArgumentCaptor.forClass(List.class);

        kafkakafkaOperations.sendDefault(new SalesLog(
                "test-sales-log-id-1",
                LocalDateTime.of(2024, 9, 15, 10, 10, 10),
                0,
                0,
                10000,
                List.of(new SalesLog.Item(2000, "FalconBlade", 9800, 1, 9800),
                        new SalesLog.Item(2002, "PotLid", 40, 5, 200)),
                new SalesLog.Customer(100, "Ortega", "male"),
                10000,
                10
        ));

        if (!latch.await(5000L, TimeUnit.MILLISECONDS)) {
            Assertions.fail("Latch timed out by testListenKafkaMessage");
        }

        Mockito.verify(salesEventApplicationService, Mockito.timeout(1)).register(salesCaptor.capture(), Mockito.any());

        List<Sales> consumedSales = salesCaptor.getValue();
        Assertions.assertThat(consumedSales).isNotNull();
        Assertions.assertThat(consumedSales).hasSize(1);

        Assertions.assertThat(consumedSales)
                .usingRecursiveComparison()
                .isNotNull();
    }
}
