//package com.example.presentation;
//
//import com.example.application.SalesEventApplicationService;
//import com.example.domain.entity.Sales;
//import com.example.domain.entity.SalesLog;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.KafkaContainer;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.containers.wait.strategy.Wait;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicReference;
//
//@SpringBootTest
//@DirtiesContext
//@Testcontainers
//class SalesLogConsumerTest {
//    @Container
//    public static KafkaContainer kafkaContainer = new KafkaContainer(
//            DockerImageName.parse("confluentinc/cp-kafka:6.2.1")
//    );
//
//    @Container
//    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(
//            DockerImageName.parse("postgres"));
//
//    @BeforeAll
//    static void startContainer() {
//        kafkaContainer.start();
//        postgreSQLContainer.start();
//
//        try {
//            if (!kafkaContainer.isRunning()) {
//                kafkaContainer.waitingFor(Wait.forLogMessage(".*Kafka started.*", 1));
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Kafka container failed to start", e);
//        }
//    }
//
//    @DynamicPropertySource
//    static void dynamicProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//        registry.add("spring.sql.init.mode", () -> "always");
//
//        // シリアライズ設定
//        registry.add("spring.kafka.producer.value-serializer", () -> "org.springframework.kafka.support.serializer.JsonSerializer");
//        registry.add("spring.kafka.consumer.value-deserializer", () -> "org.springframework.kafka.support.serializer.JsonDeserializer");
//        registry.add("spring.kafka.consumer.properties.spring.json.value.default.type", () -> "com.example.domain.entity.SalesLog");
//        registry.add("spring.kafka.consumer.properties.spring.json.trusted.packages", () -> "*");
//    }
//
//    @MockBean
//    private SalesEventApplicationService salesEventApplicationService;
//
//    @Autowired
//    private KafkaTemplate<String, SalesLog> kafkaTemplate;
//
//    @Test
//    void testListenKafkaMessage() throws Exception {
//        final AtomicReference<List<Sales>> atomicReference = new AtomicReference<>();
//        final CountDownLatch latch = new CountDownLatch(1);
//
//        // モック: saveAllSales が呼ばれた際に atomicReference に Sales をセットし、latch.countDown() で待機を解除
//        Mockito.doAnswer(invocationOnMock -> {
//            List<Sales> salesList = invocationOnMock.getArgument(0);  // 引数を List<Sales> として取得
//            atomicReference.set(salesList);  // List<Sales> を atomicReference に設定
//            latch.countDown();
//            return null;
//        }).when(salesEventApplicationService).register(Mockito.any(), Mockito.any());
//
//        SalesLog salesLog = new SalesLog(
//                "test-sales-log-id-1",
//                LocalDateTime.of(2024, 9, 15, 10, 10, 10),
//                0,
//                0,
//                10000,
//                List.of(new SalesLog.Item(2000, "FalconBlade", 9800, 1, 9800),
//                        new SalesLog.Item(2002, "PotLid", 40, 5, 200)),
//                new SalesLog.Customer(100, "Ortega", "male"),
//                10000,
//                10
//        );
//
//        // Kafka メッセージ送信
//        kafkaTemplate.send("sales-log", salesLog);
//
//        if (!latch.await(20, TimeUnit.SECONDS)) {
//            Assertions.fail("Latch timed out by testListenKafkaMessage");
//        }
//
//        // saveAllSales が呼ばれたことを検証
//        Mockito.verify(salesEventApplicationService).register(Mockito.any(), Mockito.any());
//
//        List<Sales> consumedSales = atomicReference.get();
//        Assertions.assertThat(consumedSales).isNotNull();
//        Assertions.assertThat(consumedSales).hasSize(1);  // 1件の Sales が消費されることを確認
//
//        // 必要に応じて消費された Sales の内容を検証
//        Assertions.assertThat(consumedSales)
//                .usingRecursiveComparison()
//                .isNotNull();
//    }
//}
