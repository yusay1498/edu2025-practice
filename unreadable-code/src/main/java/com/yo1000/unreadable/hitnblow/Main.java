package com.yo1000.unreadable.hitnblow;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 何も入力されない場合の桁数
        int digits = 4;
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