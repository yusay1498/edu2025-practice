package com.example.point.api.application;

public class CustomerPointNotFoundException extends RuntimeException {
    public CustomerPointNotFoundException(String id) {
        super("Customer not found: " + id);
    }
}