package com.example.sales.api.domain.entity;

public record SalesItems(
        String id,
        int itemId,
        int unitPrice,
        int quantity,
        int subtotal
) {
}
