package com.yo1000.unreadable.hitnblow;

import java.security.SecureRandom;
import java.util.random.RandomGenerator;

public class AnswerHandler {
    private final RandomGenerator random = new SecureRandom();
    private final int digits;

    public AnswerHandler(int digits) {
        this.digits = digits;
    }

    public String createAnswer() {
        int min = 0;
        int max = (int) Math.pow(10, digits) - 1;

        // min から max までの範囲の乱数を生成
        // この実装にしてあとから最小値最大値を変えれるようにする
        int answer = random.nextInt(max - min + 1) + min;

        return String.format("%0" + digits + "d", answer);
    }
}
