package com.example.sales.api.application.service;

import com.example.sales.api.domain.entity.Sales;
import com.example.sales.api.domain.repository.SalesHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SalesHistoryApplicationService {
    private final SalesHistoryRepository salesHistoryRepository;

    public SalesHistoryApplicationService(SalesHistoryRepository salesHistoryRepository) {
        this.salesHistoryRepository = salesHistoryRepository;
    }

    public Page<Sales> listSalesHistory(Pageable pageable) {
        return salesHistoryRepository.findAll(pageable);
    }
}
