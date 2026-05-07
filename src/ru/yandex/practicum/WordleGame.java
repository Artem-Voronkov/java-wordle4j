package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordleGame {
    private final WordleDictionary dictionary;
    private final PrintWriter log;
    private String answer;
    private int attemptsLeft;
    private boolean gameOver;
    private boolean won;
    List<WordleDictionary.GuessResult> previousGuessResults;
    Map<Character, Boolean> knownLetters;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;
        resetGame();
    }

    public void resetGame() {
        answer = dictionary.getRandomWord();
        dictionary.setAnswer(answer);
        attemptsLeft = 6;
        gameOver = false;
        won = false;
        previousGuessResults = new ArrayList<>();
        knownLetters = new HashMap<>();
        log.println("Игра начата. Загадано слово: " + answer);
    }

    public String makeGuess(String guess) throws WordNotFoundInDictionary {
        guess = dictionary.toLower(guess);

        if (!dictionary.contains(guess)) {
            throw new WordNotFoundInDictionary("Слово '" + guess + "' не найдено в словаре");
        }

        attemptsLeft--;

        String result = dictionary.analyzeMatch(guess, answer);
        previousGuessResults.add(new WordleDictionary.GuessResult(guess, result));
        log.println("Попытка: " + guess + " -> " + result);

        if (guess.equals(answer)) {
            won = true;
            gameOver = true;
        } else if (attemptsLeft == 0) {
            gameOver = true;
        }

        updateKnownLetters(guess, result);
        return result;
    }

    public void updateKnownLetters(String guess, String result) {
        char[] guessChars = guess.toCharArray();
        char[] resultChars = result.toCharArray();

        for (int i = 0; i < 5; i++) {
            char letter = guessChars[i];
            switch (resultChars[i]) {
                case '+':
                    knownLetters.put(letter, true);
                    break;
                case '^':
                    knownLetters.put(letter, false);
                    break;
            }
        }
    }

    public String getHint() {
        List<WordleDictionary.GuessResult> guessResults = new ArrayList<>(previousGuessResults);
        List<WordleDictionary.LetterConstraint> letterConstraints = new ArrayList<>();
        for (Map.Entry<Character, Boolean> entry : knownLetters.entrySet()) {
            letterConstraints.add(new WordleDictionary.LetterConstraint(entry.getKey(), null));
        }

        List<String> possibleWords = dictionary.filterByConstraints(guessResults, letterConstraints);
        if (!possibleWords.isEmpty()) {
            return possibleWords.get(0);
        }
        return dictionary.getRandomWord();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean hasWon() {
        return won;
    }

    public String getAnswer() {
        return answer;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public List<WordleDictionary.GuessResult> getPreviousGuessResults() {
        return new ArrayList<>(previousGuessResults);
    }

    public Map<Character, Boolean> getKnownLetters() {
        return new HashMap<>(knownLetters);
    }
}
