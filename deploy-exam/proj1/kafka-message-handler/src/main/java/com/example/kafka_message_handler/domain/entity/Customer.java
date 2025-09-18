package com.example.kafka_message_handler.domain.entity;

public record Customer(
        int id,
        String name,
        String gender
) {
}