package jp.co.isetanmitsukoshi.handson.kafka.chat;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChatMessageContainer {
    private static ChatMessageContainer singleton = new ChatMessageContainer();

    private Map<String, List<ChatMessage>> m = new ConcurrentHashMap<>();

    public static ChatMessageContainer getInstance() {
        return singleton;
    }

    private ChatMessageContainer() {}

    public synchronized void addMessage(ChatMessage chatMessage) {
        List<ChatMessage> list = m.getOrDefault(chatMessage.room(), new ArrayList<>());
        list.add(chatMessage);
        m.put(chatMessage.room(), list);
    }

    public List<ChatMessage> getMessages() {
        return m.values().stream().flatMap(Collection::stream).toList();
    }

    public List<ChatMessage> getMessages(String room) {
        return Collections.unmodifiableList(m.getOrDefault(room, Collections.emptyList()));
    }
}
