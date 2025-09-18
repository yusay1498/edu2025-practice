package com.example.kafka_message_handler.domain.entity;

public record SalesItem(
        int id,
        String name,
        int unitPrice,
        int quantity,
        int subtotal
) {
}
