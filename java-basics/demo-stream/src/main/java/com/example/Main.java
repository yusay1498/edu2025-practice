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
    }
}