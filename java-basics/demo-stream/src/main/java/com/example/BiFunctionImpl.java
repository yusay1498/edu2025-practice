package com.example;

import java.util.function.BiFunction;

public class BiFunctionImpl implements BiFunction<EmployeeMemoryRepository, Employee, EmployeeMemoryRepository> {
    @Override
    public EmployeeMemoryRepository apply(EmployeeMemoryRepository employeeMemoryRepository, Employee employee) {
        employeeMemoryRepository.saveEmployee(employee);
        return employeeMemoryRepository;
    }
}
