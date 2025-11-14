package com.example.sales.api.domain.repository;

import com.example.sales.api.domain.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SalesHistoryRepository {
    Page<Sales> findAll(Pageable pageable);
}
