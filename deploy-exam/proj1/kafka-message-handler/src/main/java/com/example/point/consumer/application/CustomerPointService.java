package com.example.point.consumer.application;

import com.example.point.consumer.domain.entity.CustomerPoint;
import com.example.point.consumer.domain.entity.Sales;
import com.example.point.consumer.domain.repository.CustomerPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerPointService {
    private final CustomerPointRepository customerPointRepository;

    public CustomerPointService(CustomerPointRepository customerPointRepository) {
        this.customerPointRepository = customerPointRepository;
    }

    public void calculateCustomerPoint(Sales sales) {
        customerPointRepository.findByCustomerId(
                        String.valueOf(sales.customer().id()))
                .ifPresentOrElse(customerPoint -> {
                    customerPointRepository.save(
                            new CustomerPoint(
                                    customerPoint.customerId(),
                                    customerPoint.point() + sales.givenPoint() - sales.paidPoint()
                            )
                    );
                }, () -> {
                    customerPointRepository.save(
                            new CustomerPoint(
                                    String.valueOf(sales.customer().id()),
                                    sales.givenPoint() - sales.paidPoint()
                            )
                    );
                });
    }
}
