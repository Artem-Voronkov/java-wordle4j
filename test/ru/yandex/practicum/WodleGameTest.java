package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private WordleDictionary dictionary;
    private PrintWriter log;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(Arrays.asList("ягода", "яблок", "груша", "слива"));
        log = new PrintWriter(System.out);
        game = new WordleGame(dictionary, log);
    }

    @Test
    void testMakeGuess_CorrectWord() throws WordNotFoundInDictionary {
        String result = game.makeGuess("яблок");
        assertEquals("+++++", result);
        assertTrue(game.hasWon());
    }

    @Test
    void testMakeGuess_IncorrectWord() throws WordNotFoundInDictionary {
        String result = game.makeGuess("груша");
        assertFalse(result.equals("+++++"));
        assertFalse(game.hasWon());
    }

    @Test
    void testMakeGuess_WordNotInDictionary() {
        assertThrows(WordNotFoundInDictionary.class, () -> game.makeGuess("апельсин"));
    }

    @Test
    void testGetHint_AfterGuesses() throws WordNotFoundInDictionary {
        game.makeGuess("груша");
        String hint = game.getHint();
        assertNotNull(hint);
    }

    @Test
    void testResetGame() throws WordNotFoundInDictionary {
        game.makeGuess("груша");
        game.resetGame();
        assertEquals(6, game.getAttemptsLeft());
        assertFalse(game.isGameOver());
    }

    @Test
    void testUpdateKnownLetters() throws WordNotFoundInDictionary {
        game.makeGuess("ягода");
        Map<Character, Boolean> known = game.getKnownLetters();
        assertTrue(known.containsKey('я'));
    }


}

