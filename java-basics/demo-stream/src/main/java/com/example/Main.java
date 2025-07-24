package com.example;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        System.out.println("#####Lambda#####");
        EmployeeMemoryRepository employeeMemoryRepository2 = employees.stream()
                .reduce(
                        new EmployeeMemoryRepository(),
                        new BiFunctionImpl(),
                        (acc1, acc2) -> acc1
                );

        EmployeeMemoryRepository employeeMemoryRepository3 = employees.stream()
                .reduce(
                        new EmployeeMemoryRepository(),
                        new BiFunction<EmployeeMemoryRepository, Employee, EmployeeMemoryRepository>() {
                            @Override
                            public EmployeeMemoryRepository apply(EmployeeMemoryRepository employeeMemoryRepository, Employee employee) {
                                employeeMemoryRepository.saveEmployee(employee);
                                return employeeMemoryRepository;
                            }
                        },
                        (acc1, acc2) -> acc1
                );

        System.out.println("#####collect#####");
        Map<String, Integer> feeMap = employees.stream()
                .collect(Collectors.toMap(
                        employee -> employee.name(),
                        employee -> employee.fee()
                ));
        System.out.println(feeMap);

        System.out.println("#####toList#####");
        System.out.println("#####Collectors.toList#####");
        List<Employee> femaleEmployees = employees.stream()
                .filter(employee -> employee.gender().equals(Gender.FEMALE))
                .collect(Collectors.toList());
        System.out.println(femaleEmployees);

        femaleEmployees.add(alice);
        System.out.println(femaleEmployees);

        System.out.println("#####Comparators.toUnmodifiableList#####");
        List<Employee> maleEmployees = employees.stream()
                .filter(employee -> employee.gender().equals(Gender.MALE))
                .collect(Collectors.toUnmodifiableList());
        System.out.println(maleEmployees);

        System.out.println("#####Stream.toList#####");
        List<Employee> systemDepartmentEmployees = employees.stream()
                .filter(employee -> employee.department().equals("システム部"))
                .toList();
        System.out.println(systemDepartmentEmployees);

        System.out.println("#####Collectors.toUnmodifiableList と Stream.toListの違い#####");
        List<Employee> nullableEmployees = Stream.of(alice, alice, alice, alice, alice).toList();
        System.out.println(nullableEmployees);

        List<Employee> nullSafeEmployees = Stream.of(alice, alice, alice, alice, alice).collect(Collectors.toUnmodifiableList());
        System.out.println(nullSafeEmployees);
    }
}