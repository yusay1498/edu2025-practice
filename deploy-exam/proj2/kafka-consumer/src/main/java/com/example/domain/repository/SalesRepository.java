package com.example.domain.repository;

import com.example.domain.entity.Sales;

import java.util.List;

public interface SalesRepository {
   List<Sales> saveAll(List<Sales> sales);
}
