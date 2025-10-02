package com.example.point.consumer.domain.entity;

public record CustomerPoint(
        String customerId,
        int currentPoints
) {
}
