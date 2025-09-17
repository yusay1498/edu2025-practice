package jp.co.isetanmitsukoshi.handson.kafka.purchase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PurchaseProducerRestController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseProducerRestController.class);

    private final KafkaOperations<String, Purchase> kafkaOps;

    public PurchaseProducerRestController(
            KafkaTemplate<String, Purchase> kafkaTemplate
    ) {
        this.kafkaOps = kafkaTemplate;
    }

    @PostMapping
    public void post(
            @RequestBody
            Purchase purchase
    ) {
        logger.info("Produce (Request): Purchase={}",
                purchase);

        kafkaOps.sendDefault(purchase.name(), purchase);
    }
}
