package jp.co.isetanmitsukoshi.handson.kafka.chat;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class ChatMessageViewer {
    @GetMapping("/{room}")
    public List<ChatMessage> get(
            @PathVariable
            String room
    ) {
        return ChatMessageContainer.getInstance().getMessages(room);
    }
}
