package com.example.kafka.consumer.domain.entity;

public record SalesItems(
        String id,
        int itemId,
        int unitPrice,
        int quantity,
        int subtotal
) {
}
