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

        String answer = answerHandler.createAnswer();

        while (true) {
            System.out.print("> ");
            String userAnswer = scanner.nextLine();

            if (!userAnswerHandler.validate(userAnswer)) continue;

            HitAndBlow hitAndBlow = hitAndBlowCounter.count(answer, userAnswer);

            System.out.println("turn | " + hitAndBlow.turn());
            System.out.println("hit  | " + hitAndBlow.hit());
            System.out.println("blow | " + hitAndBlow.blow());

            if (hitAndBlow.hit() == digits) {
                System.out.println("\nCongrats!");
                return;
            }
        }
    }
}
