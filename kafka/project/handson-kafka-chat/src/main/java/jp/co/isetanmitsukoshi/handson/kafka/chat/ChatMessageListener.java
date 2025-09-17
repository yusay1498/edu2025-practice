package jp.co.isetanmitsukoshi.handson.kafka.chat;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ChatMessageListener {
    @KafkaListener(topics = "${spring.kafka.template.default-topic}")
    public void receiveMessage(ChatMessage chatMessage) {
        ChatMessageContainer.getInstance().addMessage(chatMessage);
    }
}
