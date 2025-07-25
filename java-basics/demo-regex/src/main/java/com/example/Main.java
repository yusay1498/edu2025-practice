package com.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static boolean matches;
    private static Matcher matcher;

    public static void main(String[] args) {
        String postName = "111-1111";

        Pattern pattern = Pattern.compile("(?<zen>[0-9]+)-(?<kou>[0-9]+)");
        Matcher matcher = pattern.matcher(postName);

        if (matcher.find()) {
            System.out.println(matcher.group(0)); // 2回目なのでfalse、実行されない
        }
    }
}