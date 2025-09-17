package jp.co.isetanmitsukoshi.handson.kafka.chat;

public record ChatMessage(
        String id,
        String room,
        String username,
        String message
) {}
