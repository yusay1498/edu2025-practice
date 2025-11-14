package com.example.sales.api.domain.entity;

import java.time.LocalDate;

public record SalesSummary(
        String id,
        int itemId,
        LocalDate date,
        int totalAmount,
        int totalQuantity
) {
}
