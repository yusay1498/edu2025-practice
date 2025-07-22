package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("record Class");
        Employee alice = new Employee(
                1,
                "alice",
                25,
                "経理部",
                250000
        );
        System.out.println("alice -> " + alice);
    }
}