package com.example;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            final int index = i; // ラムダ式や内部クラスで使うにはfinal
            new Thread(() -> {
                System.out.println("Thread " + index + " started");
                // ここに並列処理を書きます
            }).start();
        }
        
    }
}