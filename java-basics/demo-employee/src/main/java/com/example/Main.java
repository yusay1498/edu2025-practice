package com.example;

import java.util.*;

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

        System.out.println("#####Collection#####");
        List<Employee> employees = List.of(
                new Employee(1, "alice", 25, "経理部", 250000, Gender.FEMALE),
                new Employee(2, "bob", 30, "システム部", 300000, Gender.MALE),
                new Employee(3, "carol", 35, "経理部", 350000, Gender.FEMALE),
                new Employee(4, "dave", 40, "システム部", 400000, Gender.MALE),
                new Employee(5, "eve", 45, "システム部", 500000, Gender.OTHER)
        );

        for (Employee employee : employees) {
            System.out.println(employee.name());
        }

        List<Employee> emptyList = Collections.emptyList();
        for (Employee employee : emptyList) {
            System.out.println(employee.name());
        }

        System.out.println("#####Map.of#####");
        Map<String, Integer> feeMap = Map.of(
                "alice", 250000,
                "bob", 300000,
                "carol", 350000,
                "dave", 400000,
                "eve", 500000
        );

        for (Map.Entry<String, Integer> entry : feeMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("#####ArrayList#####");
        List<Employee> employeeArrayList = new ArrayList<>();
        employeeArrayList.add(alice);
        employeeArrayList.add(bob);
        employeeArrayList.add(carol);
        employeeArrayList.add(david);

        for (Employee employee : employeeArrayList) {
            System.out.println(employee.name());
        }

        System.out.println("#####LinkedList####");
        List<Employee> employeeLinkedList = new LinkedList<>();
        employeeLinkedList.add(alice);
        employeeLinkedList.add(bob);
        employeeLinkedList.add(carol);
        employeeLinkedList.add(david);

        for (Employee employee : employeeLinkedList) {
            System.out.println(employee.name());
        }

        System.out.println("#####Set#####");
        System.out.println("#####HashSet#####");
        Set<Employee> employeeHashSet = new HashSet<>();
        employeeHashSet.add(alice);
        employeeHashSet.add(bob);
        employeeHashSet.add(carol);
        employeeHashSet.add(david);
        for (Employee employee : employeeHashSet) {
            System.out.println(employee.name());
        }

        System.out.println("#####LinkedHashSet#####");
        Set<Employee> employeeLinkedHashSet = new LinkedHashSet<>();
        employeeLinkedHashSet.add(alice);
        employeeLinkedHashSet.add(bob);
        employeeLinkedHashSet.add(carol);
        employeeLinkedHashSet.add(david);
        for (Employee employee : employeeLinkedHashSet) {
            System.out.println(employee.name());
        }

        System.out.println("#####TreeSet#####");
        Set<String> employeeTreeSet = new TreeSet<>();
        employeeTreeSet.add(alice.name());
        employeeTreeSet.add(bob.name());
        employeeTreeSet.add(carol.name());
        employeeTreeSet.add(david.name());
        for (String employeeName : employeeTreeSet) {
            System.out.println(employeeName);
        }
    }
}