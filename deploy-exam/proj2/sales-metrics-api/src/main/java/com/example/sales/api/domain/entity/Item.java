package com.example.sales.api.domain.entity;

public record Item(
        Integer id,
        String name,
        Integer price,
        Integer sellPrice
) {
}
