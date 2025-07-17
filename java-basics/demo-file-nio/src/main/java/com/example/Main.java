package com.example;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path path = Path.of("./demo-file-nio/src/main/resources/test.txt");

        try (BufferedReader in = Files.newBufferedReader(path)) {
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}