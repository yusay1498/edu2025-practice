package com.example.kafka.consumer.domain.repository;

import com.example.kafka.consumer.domain.entity.Sales;

import java.util.List;

public interface SalesRepository {
   List<Sales> saveAll(List<Sales> sales);
}
