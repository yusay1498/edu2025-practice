package com.example.point.api.domain.entity;

public record CustomerPoint(
        String customerId,
        int currentPoints
) {
}
