package com.example;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("fuga.txt");

        System.out.println(path);
        System.out.println(path.toAbsolutePath());

        Path path2 = Path.of("");
        System.out.println(path2.toAbsolutePath());
    }
}