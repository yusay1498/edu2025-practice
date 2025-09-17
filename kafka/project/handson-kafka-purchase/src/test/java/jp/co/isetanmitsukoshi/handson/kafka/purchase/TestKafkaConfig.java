package jp.co.isetanmitsukoshi.handson.kafka.purchase;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TestKafkaConfig {
    @Bean
    NewTopic newTopic(KafkaProperties kafkaProps) {
        return TopicBuilder.name(kafkaProps.getTemplate().getDefaultTopic()).build();
    }
}
