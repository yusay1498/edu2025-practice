package com.example;

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
                -1,
                "bob",
                30,
                "システム部",
                300000,
                Gender.MALE
        );
        System.out.println("bob -> " + bob);
    }
}