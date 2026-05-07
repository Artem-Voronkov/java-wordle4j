package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {
    private List<String> words;
    private Set<String> wordSet; // Для быстрого поиска
    private String answer;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>();
        this.wordSet = new HashSet<>();

        for (String word : words) {
            String lower = toLower(word);
            if (lower.length() == 5) {
                this.words.add(lower);
                this.wordSet.add(lower);
            }
        }
    }

    public String toLower(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return word.toLowerCase()
                .replace('Ё', 'ё')
                .replace('Е', 'е');
    }

    public boolean contains(String word) {
        String normalize = toLower(word);
        return wordSet.contains(normalize);
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new RuntimeException("Словарь пуст");
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size()));
    }

    public String analyzeMatch(String guess, String answer) {
        char[] guessChars = guess.toCharArray();
        char[] answerChars = answer.toCharArray();
        StringBuilder result = new StringBuilder("-----"); // Инициализируем все как '-'
        boolean[] usedInAnswer = new boolean[5];

        // Первый проход: отмечаем точные совпадения (+)
        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == answerChars[i]) {
                result.setCharAt(i, '+');
                usedInAnswer[i] = true;
            }
        }

        // Второй проход: ищем частичные совпадения (^)
        for (int i = 0; i < 5; i++) {
            if (result.charAt(i) == '-') { // Только если ещё не отмечено как '+'
                for (int j = 0; j < 5; j++) {
                    if (!usedInAnswer[j] && guessChars[i] == answerChars[j]) {
                        result.setCharAt(i, '^');
                        usedInAnswer[j] = true;
                        break;
                    }
                }
            }
        }
        return result.toString();
    }

    public List<String> filterByConstraints(List<String> previousGuesses,
                                            Map<Character, Boolean> knownLetters) {
        List<String> result = new ArrayList<>();

        for (String word : words) {
            boolean matchesAll = true;

            // Проверяем соответствие всем предыдущим попыткам
            for (String guess : previousGuesses) {
                String expectedResult = analyzeMatch(guess, word);
                String actualResult = analyzeMatch(guess, this.answer);
                if (!expectedResult.equals(actualResult)) {
                    matchesAll = false;
                    break;
                }
            }

            if (!matchesAll) continue;

            // Проверяем наличие обязательных букв
            if (!containsRequiredLetters(word, knownLetters)) {
                continue;
            }

            result.add(word);
        }
        return result;
    }

    private boolean containsRequiredLetters(String word, Map<Character, Boolean> letters) {
        for (Map.Entry<Character, Boolean> entry : letters.entrySet()) {
            char letter = entry.getKey();
            boolean mustBeInCorrectPosition = entry.getValue();

            if (mustBeInCorrectPosition) {
                // Буква должна быть в конкретной позиции (например, позиция 2)
                // Здесь нужно уточнить логику — возможно, передавать не только букву, но и позицию
                // Для упрощения: проверяем, что буква есть в слове
                if (word.indexOf(letter) == -1) {
                    return false;
                }
            } else {
                // Буква просто должна присутствовать в слове
                if (word.indexOf(letter) == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setAnswer(String answer) {
        this.answer = toLower(answer);
    }

    public int size() {
        return words.size();
    }
}
