package com.example.point.api.presentation;

import com.example.point.api.application.CustomerPointService;
import com.example.point.api.domain.entity.CustomerPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/points")
public class CustomerPointRestController {
    private final CustomerPointService customerPointService;

    public CustomerPointRestController(CustomerPointService customerPointService) {
        this.customerPointService = customerPointService;
    }

    @GetMapping
    public List<CustomerPoint> getCustomerPoints() {
        return customerPointService.listCustomerPoints();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerPoint> getPointByCustomerId(
            @PathVariable String customerId
    ) {
        return ResponseEntity.ok(customerPointService.lookupCustomerPointById(customerId));
    }
}
