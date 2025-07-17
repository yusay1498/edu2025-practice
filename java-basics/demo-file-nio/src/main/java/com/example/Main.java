package com.example;

import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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

        try (PrintWriter out = new PrintWriter("./demo-file-nio/src/main/resources/data.txt")){
            out.print(100);
            out.print("\t");
            out.print("田中");
            out.print("\t");
            out.print(60.5);
            out.print(100);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}