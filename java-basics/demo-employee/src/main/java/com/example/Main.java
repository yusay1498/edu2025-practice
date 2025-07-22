package com.example;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("record Class");
        Employee alice = new Employee(
                1,
                "alice",
                25,
                "経理部",
                250000,
                Gender.FEMALE
        );
        System.out.println("alice -> " + alice);

        Employee bob = new Employee(
                1,
                "bob",
                30,
                "システム部",
                300000,
                Gender.MALE
        );
        System.out.println("bob -> " + bob);

        System.out.println("#####Generics#####");
        DemoGenerics<Employee> demoGenerics = new DemoGenerics<>(alice);
        System.out.println(demoGenerics.getSomeObject().name());

        System.out.println("#####Optional#####");
        EmployeeMemoryRepository employeeMemoryRepository = new EmployeeMemoryRepository();

        Employee carol = new Employee(
                3,
                "carol",
                35,
                "経理部",
                350000,
                Gender.FEMALE
        );
        Employee david = new Employee(
                4,
                "david",
                40,
                "システム部",
                400000,
                Gender.MALE
        );

        employeeMemoryRepository.saveEmployee(carol);
        employeeMemoryRepository.saveEmployee(david);

        Optional<Employee> employeeId3 = employeeMemoryRepository.getById(3);
        System.out.println(employeeId3.orElseThrow().name());

        Optional<Employee> employeeId1 = employeeMemoryRepository.getById(1);
        System.out.println(employeeId1.orElse(
                new Employee(
                        0,
                        "John Doe",
                        0,
                        "none",
                        0,
                        Gender.OTHER)
        ));
    }
}