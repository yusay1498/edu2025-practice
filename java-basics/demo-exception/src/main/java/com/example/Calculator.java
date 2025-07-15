package com.example;

public class Calculator {
    public int div (int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        } else {
            return a / b;
        }
    }
}
