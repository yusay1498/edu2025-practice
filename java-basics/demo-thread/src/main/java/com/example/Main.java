package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("---- start main ----");

        Thread t1 = new Thread(new Runnable1());
        t1.start();

        System.out.println("---- end main ----");
    }
}