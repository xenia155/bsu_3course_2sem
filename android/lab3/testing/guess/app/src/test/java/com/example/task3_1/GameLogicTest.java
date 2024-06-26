package com.example.task3_1;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.task3_1.GameLogic;

public class GameLogicTest {

    @Test
    public void testGuessNumber_LowerThanNeeded() {
        GameLogic gameLogic = new GameLogic();
        String result = gameLogic.guessNumber(40);
        assertEquals("Try #1: Behind", result);
    }

    @Test
    public void testGuessNumber_HigherThanNeeded() {
        GameLogic gameLogic = new GameLogic();
        String result = gameLogic.guessNumber(70);
        assertEquals("Try #1: Ahead", result);
    }

    @Test
    public void testPlayMore_ResetsGame() {
        GameLogic gameLogic = new GameLogic();
        gameLogic.playMore();
        assertFalse(gameLogic.isGameOver());
    }
}
