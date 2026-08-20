package com.yo1000.unreadable.hitnblow;

import java.util.Scanner;

public class Main {
    static private final int DEFAULT_DIGITS = 4;

    public static void main(String[] args) {
        int digits = DEFAULT_DIGITS;
        Scanner scanner = new Scanner(System.in);

        // ユーザーが桁数を指定した場合の処理
        if (args.length > 0) {
            try {
                digits = Integer.parseInt(args[0]);

                if (digits <= 0) {
                    throw new IllegalArgumentException("Digits must be a positive integer.");
                }
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid input: " + args[0] + " is not a valid number.");
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid input: " + e.getMessage());
            }
        }

        GameController gameController = new GameController(digits, scanner);
        gameController.startHitAndBlow();
    }
}
