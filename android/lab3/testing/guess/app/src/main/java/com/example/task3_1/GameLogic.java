package com.example.task3_1;

import java.util.Random;

public class GameLogic {

    private int need_num;
    private boolean game_over;
    private int tries_amount;

    public GameLogic() {
        Random rand = new Random();
        need_num = rand.nextInt(200) + 1; // [1, ..., 200]
        game_over = false;
        tries_amount = 0;
    }

    public String guessNumber(int guess) {
        tries_amount++;
        String try_str = "Try #" + tries_amount + ": ";

        if (guess < need_num) {
            return try_str + "Behind";
        } else if (guess > need_num) {
            return try_str + "Ahead";
        } else {
            game_over = true;
            return try_str + "Hit";
        }
    }

    public void playMore() {
        Random rand = new Random();
        need_num = rand.nextInt(200) + 1;
        game_over = false;
        tries_amount = 0;
    }

    public boolean isGameOver() {
        return game_over;
    }
}
