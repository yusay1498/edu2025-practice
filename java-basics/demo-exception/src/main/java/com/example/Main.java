package com.example;

public class Main {
    public static void main(String[] args) {

        Calculator calculator = new Calculator();

        int a = 10;
        int b = 0;

        try {
            int answer = calculator.div(a, b);
            System.out.println(answer);
        }  catch (ArithmeticException e) {
            System.err.println(e.getMessage());
        }
    }
}