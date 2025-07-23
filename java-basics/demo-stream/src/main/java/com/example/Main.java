package com.example;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Employee alice = new Employee(
                1,
                "alice",
                25,
                "経理部",
                250000,
                Gender.FEMALE
        );
        Employee bob = new Employee(
                2,
                "bob",
                30,
                "システム部",
                300000,
                Gender.MALE
        );
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
        Employee eve = new Employee(
                5,
                "eve",
                45,
                "法人部",
                450000,
                 Gender.MALE
        );

        List<Employee> employees = List.of(alice, bob, carol, david, eve);

        System.out.println("#####filter#####");
        employees.stream()
                .filter(employee -> employee.department().equals("システム部"))
                .forEach(System.out::println);

        System.out.println("#####anyMatch, noneMatch#####");
        boolean isOverThirtyYearsOldExist = employees.stream().anyMatch(employee -> employee.age() > 30);
        if (isOverThirtyYearsOldExist) {
            System.out.println("30歳以上の人がいます");
        }

        boolean isNotExistOverFiftyYearsOld = employees.stream().noneMatch(employee -> employee.age() > 50);
        if (isNotExistOverFiftyYearsOld) {
            System.out.println("50歳以上の人はいません");
        }

        System.out.println("#####findFirst#####");
        Optional<Employee> engineer = employees.stream().
                filter(employee -> employee.department().equals("システム部"))
                .findFirst();
        System.out.println(engineer.orElseThrow().name());

        System.out.println("#####reduce#####");
        Employee mergedEmployee = employees.stream()
                .reduce(
                        new Employee(
                                0,
                                "noname",
                                0,
                                "no department",
                                0,
                                Gender.OTHER
                        ),
                        (employee1, employee2) -> new Employee(
                                employee1.id() + employee2.id(),
                                employee1.name() + employee2.name(),
                                employee1.age() + employee2.age(),
                                employee1.department() + employee2.department(),
                                employee1.fee() + employee2.fee(),
                                employee1.gender()
                        )
                );
        System.out.println(mergedEmployee);

        EmployeeMemoryRepository employeeMemoryRepository = employees.stream()
                .reduce(
                        new EmployeeMemoryRepository(),
                        (acc, cur) -> {
                            acc.saveEmployee(cur);
                            return acc;
                        },
                        (acc1, acc2) -> acc1
                );
        Optional<Employee> employeeId1 = employeeMemoryRepository.getById(1);
        System.out.println(employeeId1.orElseThrow().name());
    }
}