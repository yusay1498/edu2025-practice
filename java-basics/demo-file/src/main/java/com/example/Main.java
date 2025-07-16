package com.example;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("/home/ubuntu/hoge/fuga.txt");

        System.out.println(path);
        System.out.println(path.getRoot());
        System.out.println(path.getParent());
        System.out.println(path.getFileName());
    }
}