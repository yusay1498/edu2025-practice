package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("temp");        // 作成するディレクトリのパス
        try {
            Files.createDirectory(path);    // ディレクトリを作成する
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}