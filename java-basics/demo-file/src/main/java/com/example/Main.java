package com.example;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception{
        Path path = Path.of("foo/bar/note.txt");        // ファイルのパス
        Files.createFile(path);                            //ファイルを作る
    }
}