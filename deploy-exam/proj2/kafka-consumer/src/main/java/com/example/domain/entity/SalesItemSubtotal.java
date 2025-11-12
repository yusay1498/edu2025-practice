package com.example.domain.entity;

import java.time.LocalDate;

public record SalesItemSubtotal(
        String id,
        int itemId,
        LocalDate date,
        int amount,
        int quantity
) {
}
