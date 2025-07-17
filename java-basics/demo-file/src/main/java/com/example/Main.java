package com.example;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception{
        Path path = Path.of("foo/bar");        // 作成するディレクトリのパス
        Files.createDirectories(path);      //複数のディレクトリを作る
    }
}