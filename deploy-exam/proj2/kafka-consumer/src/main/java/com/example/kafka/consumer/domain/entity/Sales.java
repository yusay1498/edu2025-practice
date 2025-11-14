package com.example.kafka.consumer.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

public record Sales(
        String id,
        LocalDateTime dateTime,
        int discount,
        int paidPoint,
        int paidCash,
        List<SalesItems> items,
        int total,
        int givenPoint
) {
}
