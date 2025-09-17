package jp.co.isetanmitsukoshi.handson.kafka.purchase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.StreamSupport;

@SpringBootTest
@DirtiesContext
@Testcontainers
class PurchaseProducerRestControllerTest {
    /*
     * https://java.testcontainers.org/modules/kafka/
     * https://docs.confluent.io/platform/current/installation/versions-interoperability.html#cp-and-apache-kafka-compatibility
     */
    @Container
    static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> kafkaContainer.getBootstrapServers());
    }

    @Autowired
    KafkaProperties kafkaProps;

    @Autowired
    KafkaTemplate<String, Purchase> kafkaTemplate;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName(
            """
            Given configured Kafka producing controller,
            When request with post message,
            Then message saved to Kafka topic
            """
    )
    void given_configured_Kafka_producing_controller__when_request_with_post_message__then_message_saved_to_Kafka_topic() throws Exception {
        /*
         * Given
         */
        final AtomicReference<Purchase> atomicRef = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            Map<String, Object> props = kafkaProps.buildConsumerProperties();
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "testGroup");

            try (KafkaConsumer<String, Purchase> consumer = new KafkaConsumer<>(props)) {
                consumer.subscribe(Collections.singletonList(kafkaProps.getTemplate().getDefaultTopic()));

                while (true) {
                    ConsumerRecords<String, Purchase> records = consumer.poll(Duration.ofMillis(1_000L));

                    StreamSupport.stream(records.spliterator(), false).findFirst().ifPresent(record -> {
                        atomicRef.set(record.value());
                        latch.countDown();
                    });
                }
            }
        }).start();

        /*
         * When
         */
        Purchase purchase = new Purchase(
                "food_takeout",
                "kfc red hot chicken",
                320,
                5
        );

        mockMvc.perform(MockMvcRequestBuilders
                .post("/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(purchase))
        );

        if (!latch.await(10_000L, TimeUnit.MILLISECONDS)) {
            Assertions.fail("Latch timeout" + atomicRef.get());
        }

        /*
         * Then
         */
        Assertions.assertThat(atomicRef.get()).isEqualTo(purchase);
    }
}
