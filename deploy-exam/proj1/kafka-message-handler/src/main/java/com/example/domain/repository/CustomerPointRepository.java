package com.example.domain.repository;

import com.example.domain.entity.CustomerPoint;

import java.util.Optional;

public interface CustomerPointRepository {
    Optional<CustomerPoint> findByCustomerId(String customerId);
    CustomerPoint save(CustomerPoint customerPoint);
}
