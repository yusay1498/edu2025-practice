package com.example.sales.api.application.service;

import com.example.application.dto.ItemSalesSummary;
import com.example.sales.api.domain.entity.Item;
import com.example.sales.api.domain.entity.SalesSummary;
import com.example.sales.api.domain.repository.ItemRepository;
import com.example.sales.api.domain.repository.SalesSummaryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

@Service
@Transactional
public class SalesMetricsApplicationService {
    private final ItemRepository itemRepository;
    private final SalesSummaryRepository salesSummaryRepository;
    private final Clock clock;

    public SalesMetricsApplicationService(ItemRepository itemRepository, SalesSummaryRepository salesSummaryRepository, Clock clock) {
        this.itemRepository = itemRepository;
        this.salesSummaryRepository = salesSummaryRepository;
        this.clock = clock;
    }

    public Page<ItemSalesSummary> listItemSalesSummaries(Pageable pageable) {
        Page<SalesSummary> salesSummaryList = salesSummaryRepository.findAll(pageable);

        return salesSummaryList.map(this::appendItem);
    }

    public Page<ItemSalesSummary> searchItemSalesSummariesByItemIdAndDateBetween(Integer itemId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        // startDate と endDate が null の場合、デフォルト値を設定
        LocalDate defaultStartDate = LocalDate.of(1999, 2, 10);  // 例: startDateがnullの場合は初期登録データ1999/2/11の直前
        LocalDate defaultEndDate = getToday();  // 例: endDateがnullの場合は今日の日付

        // 引数を変更せず、必要に応じて新しい変数に代入
        LocalDate actualStartDate = (startDate != null) ? startDate : defaultStartDate;
        LocalDate actualEndDate = (endDate != null) ? endDate : defaultEndDate;
        Page<SalesSummary> salesSummaryList = salesSummaryRepository.findByItemIdAndDateBetween(itemId, actualStartDate, actualEndDate, pageable);

        return salesSummaryList.map(this::appendItem);
    }

    private ItemSalesSummary appendItem(SalesSummary salesSummary) {
        Item item = itemRepository.findById(salesSummary.itemId()).orElseThrow(() -> new RuntimeException("Item not found"));

        return new ItemSalesSummary(
                item.id(),
                item.name(),
                item.price(),
                salesSummary.date(),
                salesSummary.totalAmount(),
                salesSummary.totalQuantity()
        );
    }

    private LocalDate getToday() {
        return LocalDate.now(clock);
    }
}
