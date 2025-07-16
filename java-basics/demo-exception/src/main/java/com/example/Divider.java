package com.example;

public class Divider {
    public int div (int a, int b) throws Exception {
        if (b == 0) {
            throw new Exception("Division by zero");
        } else {
            return a / b;
        }
    }
}
