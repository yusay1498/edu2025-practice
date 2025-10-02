package com.example.point.consumer.domain.entity;

public record SalesItem(
        int id,
        String name,
        int unitPrice,
        int quantity,
        int subtotal
) {
}
