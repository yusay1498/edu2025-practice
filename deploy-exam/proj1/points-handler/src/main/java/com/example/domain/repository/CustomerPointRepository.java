package com.example.domain.repository;

import com.example.domain.entity.CustomerPoint;

import java.util.List;
import java.util.Optional;

public interface CustomerPointRepository {
    List<CustomerPoint> findAll();
    Optional<CustomerPoint> findByCustomerId(String customerId);
}
