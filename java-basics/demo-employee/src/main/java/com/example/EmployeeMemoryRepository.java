package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeMemoryRepository {
    private final List<Employee> employees;

    EmployeeMemoryRepository() {
        this.employees = new ArrayList<>();
    }

    void saveEmployee(Employee employee) {
        employees.add(employee);
    }

    Optional<Employee> getById(int id) {
        for (Employee employee : employees) {
            if (employee.id() == id) {
                return Optional.of(employee);
            }
        }
        return Optional.empty();
    }
}
