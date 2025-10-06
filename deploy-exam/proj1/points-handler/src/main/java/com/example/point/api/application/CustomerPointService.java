package com.example.point.api.application;

import com.example.point.api.domain.entity.CustomerPoint;
import com.example.point.api.domain.repository.CustomerPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerPointService {
    private final CustomerPointRepository customerPointRepository;

    public CustomerPointService(CustomerPointRepository customerPointRepository) {
        this.customerPointRepository = customerPointRepository;
    }

    public List<CustomerPoint> listCustomerPoints() {
        return customerPointRepository.findAll();
    }

    public CustomerPoint lookupCustomerPointById(String customerId) {
        return customerPointRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }
}
