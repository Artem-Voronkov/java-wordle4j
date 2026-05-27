package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(Arrays.asList("яблок", "груша", "слива", "вишня", "персик"));
    }

    @Test
    void testContains_ValidWord() {
        assertTrue(dictionary.contains("яблок"));
        assertTrue(dictionary.contains("ЯБЛОК"));
    }

    @Test
    void testContains_InvalidWord() {
        assertFalse(dictionary.contains("апельсин"));
    }

    @Test
    void testGetRandomWord_NotEmpty() {
        assertNotNull(dictionary.getRandomWord());
    }

    @Test
    void testAnalyzeMatch_ExactMatch() {
        assertEquals("+++++", dictionary.analyzeMatch("яблок", "яблок"));
    }

    @Test
    void testAnalyzeMatch_PartialMatch() {
        assertEquals("+-^--", dictionary.analyzeMatch("ягода", "яблок"));
    }

    @Test
    void testAnalyzeMatch_NoMatch() {
        assertEquals("-----", dictionary.analyzeMatch("груша", "яблок"));
    }
}
