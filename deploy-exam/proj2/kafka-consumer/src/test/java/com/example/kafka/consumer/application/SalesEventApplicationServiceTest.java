package com.example.kafka.consumer.application;

import com.example.kafka.consumer.domain.entity.Sales;
import com.example.kafka.consumer.domain.entity.SalesItemSubtotal;
import com.example.kafka.consumer.domain.entity.SalesItems;
import com.example.kafka.consumer.domain.repository.SalesRepository;
import com.example.kafka.consumer.domain.repository.SalesItemSubtotalRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class SalesEventApplicationServiceTest {
    @Test
    void testRegister() {
        SalesRepository mockedSalesRepository = Mockito.mock(SalesRepository.class);
        SalesItemSubtotalRepository mockSalesConsumerRepository = Mockito.mock(SalesItemSubtotalRepository.class);

        List<Sales> salesList = List.of(new Sales(
                "test-sales-id-100", LocalDateTime.of(1999, 9, 15, 10, 10, 0), 0, 0, 10000,
                List.of(new SalesItems("test-sales-items-id-100", 3000, 400, 20, 8000),
                        new SalesItems("test-sales-items-id-101", 3001, 1000, 2, 2000
                )), 10000, 10
        ), new Sales(
                "test-sales-id-101", LocalDateTime.of(1999, 9, 15, 10, 10, 0), 0, 0, 10000,
                List.of(new SalesItems("test-sales-items-id-102", 3002, 4000, 2, 8000),
                        new SalesItems("test-sales-items-id-103", 3003, 100, 20, 2000
                )), 10000, 10
        ));

        List<SalesItemSubtotal> salesItemSubtotalList = List.of(
                new SalesItemSubtotal("test-sales-summary-11", 5003, LocalDate.of(1999, 9, 15), 20000, 200),
                new SalesItemSubtotal("test-sales-summary-12", 5004, LocalDate.of(1999, 9, 15), 10000, 200),
                new SalesItemSubtotal("test-sales-summary-13", 5005, LocalDate.of(1999, 9, 15), 20000, 100)
        );

        Mockito.doReturn(salesList).when(mockedSalesRepository).saveAll(salesList);
        Mockito.doReturn(salesItemSubtotalList).when(mockSalesConsumerRepository).saveAll(salesItemSubtotalList);

        SalesEventApplicationService salesEventApplicationService = new SalesEventApplicationService(mockedSalesRepository, mockSalesConsumerRepository);
        salesEventApplicationService.register(salesList, salesItemSubtotalList);

        Mockito.verify(mockedSalesRepository, Mockito.times(1)).saveAll(Mockito.anyList());
        Mockito.verify(mockedSalesRepository, Mockito.times(1)).saveAll(salesList);
        Mockito.verify(mockedSalesRepository).saveAll(Mockito.argThat(existingSalesList -> existingSalesList.size() == salesList.size()));

        Mockito.verify(mockSalesConsumerRepository, Mockito.times(1)).saveAll(Mockito.anyList());
        Mockito.verify(mockSalesConsumerRepository, Mockito.times(1)).saveAll(salesItemSubtotalList);
        Mockito.verify(mockSalesConsumerRepository).saveAll(Mockito.argThat(existingSalesSummaryList -> existingSalesSummaryList.size() == salesItemSubtotalList.size()));
    }
}
