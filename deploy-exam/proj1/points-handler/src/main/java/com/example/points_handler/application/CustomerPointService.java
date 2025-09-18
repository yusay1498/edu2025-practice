package com.example.points_handler.application;

import com.example.points_handler.domain.entity.CustomerPoint;
import com.example.points_handler.domain.repository.CustomerPointRepository;
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

    public List<CustomerPoint> viewAllCustomerPoints() {
        return customerPointRepository.findAll();
    }

    public Optional<CustomerPoint> viewCustomerPointById(String customerId) {
        return customerPointRepository.findByCustomerId(customerId);
    }
}
