package com.example.kafka_message_handler.domain.repository;

import com.example.kafka_message_handler.domain.entity.CustomerPoint;

import java.util.Optional;

public interface CustomerPointRepository {
    Optional<CustomerPoint> findByCustomerId(String customerId);
    CustomerPoint save(CustomerPoint customerPoint);
}
