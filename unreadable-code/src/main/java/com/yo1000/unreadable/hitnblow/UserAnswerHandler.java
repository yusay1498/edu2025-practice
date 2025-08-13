package com.yo1000.unreadable.hitnblow;

public class UserAnswerHandler {
    private final int digits;

    // コンストラクタでScannerを受け取り、インスタンスフィールドに設定
    public UserAnswerHandler(int digits) {
        this.digits = digits;
    }

    // ヒットアンドブローのフォーマットに入力があっているか判定
    public boolean isValidInput(String userAnswer) {
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
