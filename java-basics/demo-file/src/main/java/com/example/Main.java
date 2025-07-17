package com.example;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) throws Exception{
        Path source = Path.of("foo/bar/note.txt");        // コピー元
        Path target = Path.of("temp/note.txt");           // コピー先
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
}