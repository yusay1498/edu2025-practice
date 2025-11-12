package com.example.application;

import com.example.domain.entity.Sales;
import com.example.domain.entity.SalesItemSubtotal;
import com.example.domain.repository.SalesRepository;
import com.example.domain.repository.SalesItemSubtotalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalesEventApplicationService {
    private final Logger logger = LoggerFactory.getLogger(SalesEventApplicationService.class);
    private final SalesRepository salesRepository;
    private final SalesItemSubtotalRepository salesItemSubtotalRepository;

    public SalesEventApplicationService(
            SalesRepository salesRepository,
            SalesItemSubtotalRepository salesItemSubtotalRepository
    ) {
        this.salesRepository = salesRepository;
        this.salesItemSubtotalRepository = salesItemSubtotalRepository;
    }

    public void register(List<Sales> sales, List<SalesItemSubtotal> salesItemSubtotalList) {
        try {
            List<Sales> result = salesRepository.saveAll(sales);
            logger.debug("Sales saved successfully: {} records.", result.size());
        } catch (Exception e) {
            logger.error("Error occurred while saving sales.", e);
        }

        try {
            List<SalesItemSubtotal> result = salesItemSubtotalRepository.saveAll(salesItemSubtotalList);
            logger.debug("SalesItemsSubtotal saved successfully: {} records.", result.size());
        } catch (Exception e) {
            logger.error("Error occurred while saving sales items subtotal.", e);
        }
    }
}
