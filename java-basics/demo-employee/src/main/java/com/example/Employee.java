package com.example;

public record Employee(
        int id,
        String name,
        int age,
        String department,
        int fee,
        Gender gender
) {
    public Employee {
        if (id < 0) throw new IllegalArgumentException("Employee id cannot be less than 0");
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Employee name cannot be null or empty");
        if (age < 0) throw new IllegalArgumentException("Employee age cannot be less than 0");
        if (department == null || department.isEmpty()) throw new IllegalArgumentException("Employee department cannot be null or empty");
        if (fee < 0) throw new IllegalArgumentException("Employee fee cannot be less than 0");
    }
}
