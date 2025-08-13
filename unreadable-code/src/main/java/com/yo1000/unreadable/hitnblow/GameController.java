package com.yo1000.unreadable.hitnblow;

import java.util.Scanner;

public class GameController {
    private final int digits;
    private final Scanner scanner;
    private final AnswerHandler answerHandler;
    private final UserAnswerHandler userAnswerHandler;
    private final HitAndBlowCounter hitAndBlowCounter;

    public GameController(int digits, Scanner scanner) {
        this.digits = digits;
        this.scanner = scanner;
        this.answerHandler = new AnswerHandler(digits);
        this.userAnswerHandler = new UserAnswerHandler(digits);
        this.hitAndBlowCounter = new HitAndBlowCounter(digits);
    }

    public void startHitAndBlow() {
        System.out.println("Secret code is " + digits + "-digits.");
        System.out.println("*".repeat(digits));

        // 答えの生成
        String answer = answerHandler.createAnswer();

        while (true) {
            // ユーザーの答えの入力
            System.out.print("> ");
            String userAnswer = scanner.nextLine();

            // フォーマットに沿ってない回答の場合やり直し
            if (!userAnswerHandler.isValidInput(userAnswer)) continue;

            HitAndBlow hitAndBlow = hitAndBlowCounter.countHitAndBlow(answer, userAnswer);

            System.out.println("turn | " + hitAndBlow.turn());
            System.out.println("hit  | " + hitAndBlow.hits());
            System.out.println("blow | " + hitAndBlow.blow());

            if (hitAndBlow.hits() == digits) {
                System.out.println("\nCongrats!");
                return;
            }
        }
    }
}