package com.example;

public class Calculator {
    private final Divider divider;

    public Calculator(Divider divider) {
        this.divider = divider;
    }

    public int calculate(int a, int b) {
        return divider.div(a, b);
    }
}
