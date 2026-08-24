package com.yo1000.unreadable.hitnblow;

public class AnswerValidator {
    private final int digits;

    public AnswerValidator(int digits) {
        this.digits = digits;
    }

    public boolean validate(String userAnswer) {
        if (userAnswer.length() != digits) {
            System.out.println("Input must be " + digits + " digits long.");
            return false;
        }

        if (!userAnswer.matches("^[0-9]+$")) {
            System.out.println("Input must only contain digits.");
            return false;
        }

        return true;
    }
}
