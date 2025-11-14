package com.example.sales.api.presentation;

import com.example.sales.api.application.service.SalesHistoryApplicationService;
import com.example.sales.api.domain.entity.Sales;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salesHistories")
public class SalesHistoryRestController {
    private final SalesHistoryApplicationService salesHistoryApplicationService;

    public SalesHistoryRestController(SalesHistoryApplicationService salesHistoryApplicationService) {
        this.salesHistoryApplicationService = salesHistoryApplicationService;
    }

    @GetMapping
    public Page<Sales> getSalesHistory(Pageable pageable) {
        return salesHistoryApplicationService.listSalesHistory(pageable);
    }
}
