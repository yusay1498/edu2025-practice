package com.example.kafka.consumer.domain.entity;

import java.time.LocalDate;

public record SalesItemSubtotal(
        String id,
        int itemId,
        LocalDate date,
        int amount,
        int quantity
) {
}
