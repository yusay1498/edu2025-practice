package com.example.demo_junit_basic;

public record Employee(
        int id,
        String name,
        int age,
        String department,
        Gender gender
) {
    public Employee(int id, String name, int age, String department, Gender gender) {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid Employee ID. ID must be a positive Integer.");
        }

        this.id = id;
        this.name = name;
        this.age = age;
        this.department = department;
        this.gender = gender;
    }
}
