package com.example;

public class Divider {
    public int div (int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        } else {
            return a / b;
        }
    }
}
