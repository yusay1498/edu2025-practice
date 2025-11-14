package com.example.sales.api.presentation;

import com.example.application.dto.ItemSalesSummary;
import com.example.sales.api.application.service.SalesMetricsApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/salesSummaries")
public class ItemSalesSummaryRestController {
    private final SalesMetricsApplicationService salesMetricsApplicationService;

    public ItemSalesSummaryRestController(SalesMetricsApplicationService salesMetricsApplication) {
        this.salesMetricsApplicationService = salesMetricsApplication;
    }

    @GetMapping
    public Page<ItemSalesSummary> getItemSalesSummary(Pageable pageable) {
        return salesMetricsApplicationService.listItemSalesSummaries(pageable);
    }

    @GetMapping("/{itemId}")
    public Page<ItemSalesSummary> getItemSalesSummaryByItemIdAndDateRange(
            @PathVariable("itemId") Integer itemId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable) {
        if ((startDate != null && endDate != null) && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The startDate must be before the endDate.");
        }

        return salesMetricsApplicationService.searchItemSalesSummariesByItemIdAndDateBetween(itemId, startDate, endDate, pageable);
    }
}
