package com.example.point.consumer.application;

import com.example.point.consumer.domain.entity.Customer;
import com.example.point.consumer.domain.entity.CustomerPoint;
import com.example.point.consumer.domain.entity.Sales;
import com.example.point.consumer.domain.entity.SalesItem;
import com.example.point.consumer.domain.repository.CustomerPointRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.times;

class CustomerPointServiceTest {

    @Test
    void calculateCustomerPoint_existingCustomer() {
        CustomerPointRepository mockedRepo = Mockito.mock(CustomerPointRepository.class);

        Customer customer = new Customer(1, "Alice", "Female");
        SalesItem item1 = new SalesItem(1, "Item A", 100, 2, 200);
        SalesItem item2 = new SalesItem(2, "Item B", 150, 1, 150);
        LocalDateTime fixedDateTime = LocalDateTime.of(2024, 10, 1, 12, 0);
        Sales sales = new Sales(
                "sale_001",
                fixedDateTime,
                10,
                50,
                100,
                Arrays.asList(item1, item2),
                customer,
                300,
                150
        );

        Optional<CustomerPoint> existingCustomerPoint = Optional.of(new CustomerPoint("1", 100));
        Mockito.doReturn(existingCustomerPoint).when(mockedRepo).findByCustomerId("1");

        CustomerPointService customerPointService = new CustomerPointService(mockedRepo);
        customerPointService.calculateCustomerPoint(sales);

        Mockito.verify(mockedRepo, times(1)).save(Mockito.any(CustomerPoint.class));
        Mockito.verify(mockedRepo, times(1)).findByCustomerId("1");
        Mockito.verify(mockedRepo).save(Mockito.argThat(customerPoint -> customerPoint.currentPoints() == 200));
    }

    @Test
    void calculateCustomerPoint_newCustomer() {
        CustomerPointRepository mockedRepo = Mockito.mock(CustomerPointRepository.class);

        Customer customer = new Customer(1, "Alice", "Female");
        SalesItem item1 = new SalesItem(1, "Item A", 100, 2, 200);
        SalesItem item2 = new SalesItem(2, "Item B", 150, 1, 150);
        LocalDateTime fixedDateTime = LocalDateTime.of(2024, 10, 1, 12, 0);
        Sales sales = new Sales(
                "sale_001",
                fixedDateTime,
                10,
                50,
                100,
                Arrays.asList(item1, item2),
                customer,
                300,
                150
        );

        Optional<CustomerPoint> customerPointEmpty = Optional.empty();
        Mockito.doReturn(customerPointEmpty).when(mockedRepo).findByCustomerId("1");

        CustomerPointService customerPointService = new CustomerPointService(mockedRepo);
        customerPointService.calculateCustomerPoint(sales);

        Mockito.verify(mockedRepo, times(1)).save(Mockito.any(CustomerPoint.class));
        Mockito.verify(mockedRepo, times(1)).findByCustomerId("1");
        Mockito.verify(mockedRepo).save(Mockito.argThat(customerPoint -> customerPoint.currentPoints() == 100));
    }

}