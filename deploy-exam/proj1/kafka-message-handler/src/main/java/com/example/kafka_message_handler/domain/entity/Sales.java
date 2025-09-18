package com.example.kafka_message_handler.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record Sales(
        String id,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd HH:mm:ss") // hhをHHに変更
        LocalDateTime dateTime,
        int discount,
        int paidPoint,
        int paidCash,
        List<SalesItem> items,
        Customer customer,
        int total,
        int givenPoint
) {
}
