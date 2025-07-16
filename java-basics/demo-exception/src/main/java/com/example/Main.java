package com.example;

public class Main {
    public static void main(String[] args) {
        Divider divider = new Divider();

        Calculator calculator = new Calculator(divider);

        int a = 10;
        int b = 0;

        try {
            int answer = calculator.calculate(a, b);
            System.out.println(answer);
        }  catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println("end!");
    }
}