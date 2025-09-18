package com.example.points_handler.domain.repository;

import com.example.points_handler.domain.entity.CustomerPoint;

import java.util.List;
import java.util.Optional;

public interface CustomerPointRepository {
    List<CustomerPoint> findAll();
    Optional<CustomerPoint> findByCustomerId(String customerId);
}
