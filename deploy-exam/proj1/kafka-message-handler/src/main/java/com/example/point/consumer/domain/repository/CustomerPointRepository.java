package com.example.point.consumer.domain.repository;

import com.example.point.consumer.domain.entity.CustomerPoint;

import java.util.Optional;

public interface CustomerPointRepository {
    Optional<CustomerPoint> findByCustomerId(String customerId);
    CustomerPoint save(CustomerPoint customerPoint);
}
