package com.example.votewebback;

import java.util.Random;

public class RandomCode {
    public static String randomCode() {
        Random rnd = new Random();

        StringBuilder buf = new StringBuilder();

        int alphabetCount = 0;

        for (int i = 0; i < 6; i++) {
            if (alphabetCount < 2) {
                if (rnd.nextBoolean()) {
                    buf.append((char) ((int) (rnd.nextInt(26)) + 65));
                    alphabetCount++;
                } else {
                    buf.append((rnd.nextInt(10)));
                }
            } else {
                buf.append((rnd.nextInt(10)));
            }
        }
        return buf.toString();
    }
}