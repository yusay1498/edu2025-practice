package com.example.point.consumer.presentation;

import com.example.point.consumer.application.CustomerPointService;
import com.example.point.consumer.config.TestcontainersConfiguration;
import com.example.point.consumer.domain.entity.Customer;
import com.example.point.consumer.domain.entity.Sales;
import com.example.point.consumer.domain.entity.SalesItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@DirtiesContext
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class SalesConsumerTest {

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @MockitoBean
    private CustomerPointService customerPointService;

    @Autowired
    KafkaOperations<String, Sales> kafkaOperations;

    @Test
    public void testConsumeKafkaMessage() throws Exception {
        final AtomicReference<Sales> atomicRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        Mockito.doAnswer(invocationOnMock -> {
            atomicRef.set(invocationOnMock.getArgument(0, Sales.class));
            latch.countDown();
            return null;
        }).when(customerPointService).calculateCustomerPoint(Mockito.any());

        Customer customer = new Customer(1, "Alice", "Female");
        SalesItem item1 = new SalesItem(1, "Item A", 100, 2, 200);
        SalesItem item2 = new SalesItem(2, "Item B", 150, 1, 150);
        LocalDateTime fixedDateTime = LocalDateTime.of(2024, 10, 1, 12, 0);
        Sales sales = new Sales(
                "sale_001",
                fixedDateTime,
                10,
                50,
                100,
                Arrays.asList(item1, item2),
                customer,
                300,
                150
        );

        // Kafkaトピックにメッセージを送信
        kafkaOperations.sendDefault(sales);

        if (!latch.await(10_000L, TimeUnit.MILLISECONDS)) {
            /*
             * 10秒待っても解錠されなかった場合、どこかに設定ミス等が存在する可能性が高いため、
             * テストを失敗させて終了します。
             */
            Assertions.fail("Latch timeout");
        }

        Mockito.verify(customerPointService).calculateCustomerPoint(sales);

        Assertions.assertThat(atomicRef.get()).isEqualTo(sales);
    }


    @Test
    void testListenSuccess() {
        Customer customer = new Customer(1, "Alice", "Female");
        SalesItem item1 = new SalesItem(1, "Item A", 100, 2, 200);
        SalesItem item2 = new SalesItem(2, "Item B", 150, 1, 150);
        LocalDateTime fixedDateTime = LocalDateTime.of(2024, 10, 1, 12, 0);
        Sales sales = new Sales(
                "sale_001",
                fixedDateTime,
                10,
                50,
                100,
                Arrays.asList(item1, item2),
                customer,
                300,
                150
        );

        String topic = "test-topic";
        String groupId = "test-group";
        int offset = 0;

        SalesConsumer salesConsumer = new SalesConsumer(customerPointService);
        salesConsumer.listen(topic, groupId, offset, sales);

        Mockito.verify(customerPointService, Mockito.times(1)).calculateCustomerPoint(sales);
    }

    @Test
    void testListenError() {
        Customer customer = new Customer(1, "Alice", "Female");
        SalesItem item1 = new SalesItem(1, "Item A", 100, 2, 200);
        SalesItem item2 = new SalesItem(2, "Item B", 150, 1, 150);
        LocalDateTime fixedDateTime = LocalDateTime.of(2024, 10, 1, 12, 0);
        Sales sales = new Sales(
                "sale_001",
                fixedDateTime,
                10,
                50,
                100,
                Arrays.asList(item1, item2),
                customer,
                300,
                150
        );

        String topic = "test-topic";
        String groupId = "test-group";
        int offset = 0;

        Mockito.doThrow(new RuntimeException("Test exception")).when(customerPointService).calculateCustomerPoint(sales);
        SalesConsumer salesConsumer = new SalesConsumer(customerPointService);

        // ログ用モックを作成
        Logger mockLogger = Mockito.mock(Logger.class);
        ReflectionTestUtils.setField(salesConsumer, "logger", mockLogger);

        salesConsumer.listen(topic, groupId, offset, sales);

        // ログの検証
        Mockito.verify(mockLogger).error(
                Mockito.eq("Error processing message: Topic={}, Offset={}, Error={}"),
                Mockito.eq(topic),
                Mockito.eq(offset),
                Mockito.eq("Test exception"),
                Mockito.any(RuntimeException.class)
        );
    }

}