package com.example.points_handler.presentation;

import com.example.points_handler.application.CustomerPointService;
import com.example.points_handler.domain.entity.CustomerPoint;
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
        return customerPointService.viewAllCustomerPoints();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerPoint> getPointByCustomerId(
            @PathVariable String customerId
    ) {
        return customerPointService.viewCustomerPointById(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
