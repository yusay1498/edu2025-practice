package com.example.point.api.domain.repository;

import com.example.point.api.domain.entity.CustomerPoint;

import java.util.List;
import java.util.Optional;

public interface CustomerPointRepository {
    List<CustomerPoint> findAll();
    Optional<CustomerPoint> findByCustomerId(String customerId);
}
