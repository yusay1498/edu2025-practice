package com.example.domain.entity;

public record SalesItem(
        int id,
        String name,
        int unitPrice,
        int quantity,
        int subtotal
) {
}
