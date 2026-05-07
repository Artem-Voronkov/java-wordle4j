package ru.yandex.practicum;

import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private WordleDictionary dictionary;
    private List<String> testWords;

    @BeforeEach
    void setUp() {
        testWords = Arrays.asList("кошка", "мышка", "крыша", "книга", "ручка");
        dictionary = new WordleDictionary(testWords);
    }

    @Test
    void testContains_WordExists() {
        assertTrue(dictionary.contains("кошка"));
        assertTrue(dictionary.contains("КОШКА"));
        assertTrue(dictionary.contains("кОшКа"));
        assertTrue(dictionary.contains("кошкА"));
    }

    @Test
    void testContains_WordDoesNotExist() {
        assertFalse(dictionary.contains("слон"));
        assertFalse(dictionary.contains(""));
    }

    @Test
    void testGetRandomWord_ReturnsValidWord() {
        String randomWord = dictionary.getRandomWord();
        assertTrue(testWords.contains(randomWord));
        assertEquals(5, randomWord.length());
    }

    @Test
    void testAnalyzeMatch_ExactMatch() {
        assertEquals("+++++", dictionary.analyzeMatch("кошка", "кошка"));
    }

    @Test
    void testAnalyzeMatch_PartialMatch() {
        assertEquals("+--^+", dictionary.analyzeMatch("крыша", "кошка"));
    }

    @Test
    void testToLower_NormalizesWord() {
        assertEquals("кошка", dictionary.toLower("КОШКА"));
        assertEquals("кошка", dictionary.toLower("кОшКа"));
        assertEquals("кошка", dictionary.toLower("кошкА"));
        assertEquals("кошка", dictionary.toLower("кошка"));
        assertEquals("кошёк", dictionary.toLower("КОШЁК"));
        assertEquals("кошёк", dictionary.toLower("КоШёК"));
        assertEquals("ёжик", dictionary.toLower("ЁЖИК"));
    }
}
