package jp.co.isetanmitsukoshi.handson.kafka.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class PurchaseConsumer {
    private final Logger logger = LoggerFactory.getLogger(PurchaseConsumer.class);

    private final CalculateApplicationService calculateApplicationService;

    public PurchaseConsumer(CalculateApplicationService calculateApplicationService) {
        this.calculateApplicationService = calculateApplicationService;
    }

    // SpELによる参照でKafkaPropertiesの設定値を、@KafkaListenerアノテーションのパラメータに引き込みます
    // SpEL = Spring Expression Language
    // https://docs.spring.io/spring-framework/reference/core/expressions.html
    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void listen(
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            @Header(KafkaHeaders.GROUP_ID) String groupId,
            @Header(KafkaHeaders.OFFSET) int offset,
            @Payload Purchase purchase
    ) {
        logger.info("Consume: Topic={}, Key={}, GroupId={}, Offset={}, Purchase={}",
                topic, key, groupId, offset, purchase);

        logger.info("Subtotal: {}",
                calculateApplicationService.calculate(purchase));
    }
}
