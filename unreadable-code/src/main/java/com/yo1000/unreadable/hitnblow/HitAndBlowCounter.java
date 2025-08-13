package com.yo1000.unreadable.hitnblow;

public class HitAndBlowCounter {
    private int count = 1;
    private final int digits;

    public HitAndBlowCounter(int digits) {
        this.digits = digits;
    }

    public HitAndBlow countHitAndBlow(String answer, String userAnswer) {
        int hits = 0;

        for (int i = 0; i < digits; i++) if (userAnswer.charAt(i) == answer.charAt(i)) hits++;

        int blows = 0;

        // distinctで重複文字の排除
        // toArrayは要素を配列にする
        for (int i : userAnswer.chars().distinct().toArray()) {
            blows += (int) answer.chars().filter(c -> c == i).count();
        }

        return new HitAndBlow(hits, blows - hits, count++);
    }
}