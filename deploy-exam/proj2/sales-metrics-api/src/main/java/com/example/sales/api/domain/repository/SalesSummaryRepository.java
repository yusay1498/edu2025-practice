package com.example.sales.api.domain.repository;

import com.example.sales.api.domain.entity.SalesSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SalesSummaryRepository {
    Page<SalesSummary> findAll(Pageable pageable);
    Page<SalesSummary> findByItemIdAndDateBetween(int itemId, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
