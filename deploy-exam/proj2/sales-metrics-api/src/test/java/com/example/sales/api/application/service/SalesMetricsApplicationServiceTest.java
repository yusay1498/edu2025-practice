package com.example.sales.api.application.service;

import com.example.application.dto.ItemSalesSummary;
import com.example.sales.api.domain.entity.Item;
import com.example.sales.api.domain.entity.SalesSummary;
import com.example.sales.api.domain.repository.ItemRepository;
import com.example.sales.api.domain.repository.SalesSummaryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;
import org.assertj.core.api.Assertions;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

class SalesMetricsApplicationServiceTest {
    @Test
    void testListItemSalesSummaries() {
        List<SalesSummary> salesSummaryList = List.of(
                new SalesSummary("test-salesSummary-id-1",1500, LocalDate.of(2024, 12, 5), 400000, 200),
                new SalesSummary("test-salesSummary-id-2",1501, LocalDate.of(2024, 12, 5), 300000, 150),
                new SalesSummary("test-salesSummary-id-3",1502, LocalDate.of(2024, 12, 5), 250000, 100)
        );

        Optional<Item> item1 = Optional.of(new Item(1500, "item1", 2000, 1700));
        Optional<Item> item2 = Optional.of(new Item(1501, "item2", 2000, 100));
        Optional<Item> item3 = Optional.of(new Item(1502, "item3", 2500, 2000));

        Page<SalesSummary> salesSummaryPage = new PageImpl<>(salesSummaryList, PageRequest.of(0, 20), salesSummaryList.size());

        SalesSummaryRepository mockedSalesSummaryRepository = Mockito.mock(SalesSummaryRepository.class);
        ItemRepository mockedItemRepository = Mockito.mock(ItemRepository.class);
        Clock mockedClock = Mockito.mock(Clock.class);

        // SalesSummaryRepository のモック設定: findAll() がページを返す
        Mockito.doReturn(salesSummaryPage).when(mockedSalesSummaryRepository).findAll(PageRequest.of(0, 20));

        // ItemRepository のモック設定: findById(1500, 1501, 1502) が適切なItemを返す
        Mockito.doReturn(item1).when(mockedItemRepository).findById(1500);
        Mockito.doReturn(item2).when(mockedItemRepository).findById(1501);
        Mockito.doReturn(item3).when(mockedItemRepository).findById(1502);

        SalesMetricsApplicationService salesMetricsApplicationService = new SalesMetricsApplicationService(mockedItemRepository, mockedSalesSummaryRepository, mockedClock);

        Page<ItemSalesSummary> actual = salesMetricsApplicationService.listItemSalesSummaries(PageRequest.of(0, 20));

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getTotalElements()).isEqualTo(salesSummaryList.size());

        ItemSalesSummary firstSummary = actual.getContent().get(0);
        Assertions.assertThat(firstSummary.itemId()).isEqualTo(1500);
        Assertions.assertThat(firstSummary.itemName()).isEqualTo("item1");
        Assertions.assertThat(firstSummary.itemPrice()).isEqualTo(2000);
        Assertions.assertThat(firstSummary.totalAmount()).isEqualTo(400000);
        Assertions.assertThat(firstSummary.totalQuantity()).isEqualTo(200);

        ItemSalesSummary secondSummary = actual.getContent().get(1);
        Assertions.assertThat(secondSummary.itemId()).isEqualTo(1501);
        Assertions.assertThat(secondSummary.itemName()).isEqualTo("item2");
        Assertions.assertThat(secondSummary.itemPrice()).isEqualTo(2000);
        Assertions.assertThat(secondSummary.totalAmount()).isEqualTo(300000);
        Assertions.assertThat(secondSummary.totalQuantity()).isEqualTo(150);

        ItemSalesSummary thirdSummary = actual.getContent().get(2);
        Assertions.assertThat(thirdSummary.itemId()).isEqualTo(1502);
        Assertions.assertThat(thirdSummary.itemName()).isEqualTo("item3");
        Assertions.assertThat(thirdSummary.itemPrice()).isEqualTo(2500);
        Assertions.assertThat(thirdSummary.totalAmount()).isEqualTo(250000);
        Assertions.assertThat(thirdSummary.totalQuantity()).isEqualTo(100);

        Mockito.verify(mockedItemRepository, Mockito.times(3)).findById(Mockito.anyInt());
    }

    @Test
    void testSearchItemSalesSummariesByItemIdAndDateBetween() {
        List<SalesSummary> salesSummaryList = List.of(
                new SalesSummary("test-salesSummary-id-1", 1500, LocalDate.of(2024, 12, 5), 400000, 200),
                new SalesSummary("test-salesSummary-id-2", 1500, LocalDate.of(2024, 12, 6), 350000, 180),
                new SalesSummary("test-salesSummary-id-3", 1500, LocalDate.of(2024, 12, 7), 500000, 250)
        );

        Optional<Item> item = Optional.of(new Item(1500, "item1", 2000, 1700));

        Page<SalesSummary> salesSummaryPage = new PageImpl<>(salesSummaryList, PageRequest.of(0, 20), salesSummaryList.size());

        SalesSummaryRepository mockedSalesSummaryRepository = Mockito.mock(SalesSummaryRepository.class);
        ItemRepository mockedItemRepository = Mockito.mock(ItemRepository.class);
        Clock mockedClock = Mockito.mock(Clock.class);

        // モック設定: 現在日付を指定
        LocalDate mockToday = LocalDate.of(2024, 12, 7);
        Instant mockInstant = mockToday.atStartOfDay(ZoneId.of("UTC")).toInstant();  // 明示的に UTC ゾーンを指定
        Mockito.doReturn(mockInstant).when(mockedClock).instant();  // Clock の instant() が返す値を Instant に設定
        Mockito.doReturn(ZoneId.of("UTC")).when(mockedClock).getZone();  // Clock に ZoneId を設定

        // SalesSummaryRepository のモック設定: findByItemIdAndStartDateBetween() が指定した範囲内のページを返す
        Mockito.doReturn(salesSummaryPage)
                .when(mockedSalesSummaryRepository)
                .findByItemIdAndDateBetween(Mockito.anyInt(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(PageRequest.class));

        Mockito.doReturn(item).when(mockedItemRepository).findById(1500);

        SalesMetricsApplicationService salesMetricsApplicationService = new SalesMetricsApplicationService(mockedItemRepository, mockedSalesSummaryRepository, mockedClock);

        Page<ItemSalesSummary> actual = salesMetricsApplicationService.searchItemSalesSummariesByItemIdAndDateBetween(
                1500, LocalDate.of(2024, 12, 5), LocalDate.of(2024, 12, 7), PageRequest.of(0, 20));

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getTotalElements()).isEqualTo(salesSummaryList.size());

        ItemSalesSummary firstSummary = actual.getContent().get(0);
        Assertions.assertThat(firstSummary.itemId()).isEqualTo(1500);
        Assertions.assertThat(firstSummary.itemName()).isEqualTo("item1");
        Assertions.assertThat(firstSummary.itemPrice()).isEqualTo(2000);
        Assertions.assertThat(firstSummary.totalAmount()).isEqualTo(400000);
        Assertions.assertThat(firstSummary.totalQuantity()).isEqualTo(200);

        Mockito.verify(mockedItemRepository, Mockito.times(3)).findById(1500);
    }

    @Test
    void testViewItemSalesSummariesByItemIdAndStartDateBetweenWithNullStartDate() {
        List<SalesSummary> salesSummaryList = List.of(
                new SalesSummary("test-salesSummary-id-1", 1500, LocalDate.of(2024, 12, 5), 400000, 200),
                new SalesSummary("test-salesSummary-id-2", 1500, LocalDate.of(2024, 12, 6), 350000, 180),
                new SalesSummary("test-salesSummary-id-3", 1500, LocalDate.of(2024, 12, 7), 500000, 250)
        );

        Optional<Item> item = Optional.of(new Item(1500, "item1", 2000, 1700));

        Page<SalesSummary> salesSummaryPage = new PageImpl<>(salesSummaryList, PageRequest.of(0, 20), salesSummaryList.size());

        SalesSummaryRepository mockedSalesSummaryRepository = Mockito.mock(SalesSummaryRepository.class);
        ItemRepository mockedItemRepository = Mockito.mock(ItemRepository.class);
        Clock mockedClock = Mockito.mock(Clock.class);

        // モック設定: 現在日付を指定
        LocalDate mockToday = LocalDate.of(2024, 12, 7);
        Instant mockInstant = mockToday.atStartOfDay(ZoneId.of("UTC")).toInstant();  // 明示的に UTC ゾーンを指定
        Mockito.doReturn(mockInstant).when(mockedClock).instant();  // Clock の instant() が返す値を Instant に設定
        Mockito.doReturn(ZoneId.of("UTC")).when(mockedClock).getZone();  // Clock に ZoneId を設定

        Mockito.doReturn(salesSummaryPage)
                .when(mockedSalesSummaryRepository)
                .findByItemIdAndDateBetween(Mockito.anyInt(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(PageRequest.class));

        Mockito.doReturn(item).when(mockedItemRepository).findById(1500);

        SalesMetricsApplicationService salesMetricsApplicationService = new SalesMetricsApplicationService(mockedItemRepository, mockedSalesSummaryRepository, mockedClock);

        // startDate が null の場合にデフォルトの開始日が適用されるか確認
        Page<ItemSalesSummary> actual = salesMetricsApplicationService.searchItemSalesSummariesByItemIdAndDateBetween(
                1500, null, LocalDate.of(2024, 12, 7), PageRequest.of(0, 20));

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getTotalElements()).isEqualTo(salesSummaryList.size());

        Mockito.verify(mockedItemRepository, Mockito.times(3)).findById(1500);
    }

    @Test
    void testViewItemSalesSummariesByItemIdAndStartDateBetweenWithNullEndDate() {
        List<SalesSummary> salesSummaryList = List.of(
                new SalesSummary("test-salesSummary-id-1", 1500, LocalDate.of(2024, 12, 5), 400000, 200),
                new SalesSummary("test-salesSummary-id-2", 1500, LocalDate.of(2024, 12, 6), 350000, 180),
                new SalesSummary("test-salesSummary-id-3", 1500, LocalDate.of(2024, 12, 7), 500000, 250)
        );

        Optional<Item> item = Optional.of(new Item(1500, "item1", 2000, 1700));

        Page<SalesSummary> salesSummaryPage = new PageImpl<>(salesSummaryList, PageRequest.of(0, 20), salesSummaryList.size());

        SalesSummaryRepository mockedSalesSummaryRepository = Mockito.mock(SalesSummaryRepository.class);
        ItemRepository mockedItemRepository = Mockito.mock(ItemRepository.class);
        Clock mockedClock = Mockito.mock(Clock.class);

        // モック設定: 現在日付を指定
        LocalDate mockToday = LocalDate.of(2024, 12, 7);
        Instant mockInstant = mockToday.atStartOfDay(ZoneId.of("UTC")).toInstant();  // 明示的に UTC ゾーンを指定
        Mockito.doReturn(mockInstant).when(mockedClock).instant();  // Clock の instant() が返す値を Instant に設定
        Mockito.doReturn(ZoneId.of("UTC")).when(mockedClock).getZone();  // Clock に ZoneId を設定

        Mockito.doReturn(salesSummaryPage)
                .when(mockedSalesSummaryRepository)
                .findByItemIdAndDateBetween(Mockito.anyInt(), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class), Mockito.any(PageRequest.class));

        Mockito.doReturn(item).when(mockedItemRepository).findById(1500);

        SalesMetricsApplicationService salesMetricsApplicationService = new SalesMetricsApplicationService(mockedItemRepository, mockedSalesSummaryRepository, mockedClock);

        // endDate が null の場合にデフォルトの終了日が適用されるか確認
        Page<ItemSalesSummary> actual = salesMetricsApplicationService.searchItemSalesSummariesByItemIdAndDateBetween(
                1500, LocalDate.of(2024, 12, 5), null, PageRequest.of(0, 20));

        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getTotalElements()).isEqualTo(salesSummaryList.size());

        Mockito.verify(mockedItemRepository, Mockito.times(3)).findById(1500);
    }
}
