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
        return word.toLowerCase(Locale.forLanguageTag("ru"))
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

    public String analyzeMatch(String guess, String target) {
        StringBuilder result = new StringBuilder("-----");
        char[] targetArray = target.toCharArray();
        boolean[] used = new boolean[targetArray.length];

        // Сначала отмечаем точные совпадения (+)
        for (int count = 0; count < guess.length(); count++) {
            if (guess.charAt(count) == targetArray[count]) {
                result.setCharAt(count, '+');
                used[count] = true;
            }
        }

        // Затем отмечаем буквы, которые есть в слове, но на других позициях (^)
        for (int count = 0; count < guess.length(); count++) {
            if (result.charAt(count) != '+') { // Пропускаем уже отмеченные точные совпадения
                char c = guess.charAt(count);
                for (int j = 0; j < targetArray.length; j++) {
                    if (!used[j] && c == targetArray[j]) {
                        result.setCharAt(count, '^');
                        used[j] = true;
                        break;
                    }
                }
            }
        }
        return result.toString();
    }


    // Вспомогательный класс для хранения результатов попыток
    public static class GuessResult {
        public final String guess;
        public final String result;

        public GuessResult(String guess, String result) {
            this.guess = guess;
            this.result = result;
        }
    }

    // Класс для описания ограничений по буквам
    public static class LetterConstraint {
        public final char letter;
        public final Integer position; // null — любая позиция, число — конкретная

        public LetterConstraint(char letter, Integer position) {
            this.letter = letter;
            this.position = position;
        }
    }

    public List<String> filterByConstraints(List<GuessResult> previousGuesses, List<LetterConstraint> knownConstraints) {
        List<String> result = new ArrayList<>(words);

        // Фильтруем по результатам предыдущих попыток
        for (GuessResult guessResult : previousGuesses) {
            List<String> toRemove = new ArrayList<>();
            for (String word : result) {
                if (!analyzeMatch(guessResult.guess, word).equals(guessResult.result)) {
                    toRemove.add(word);
                }
            }
            result.removeAll(toRemove);
        }

        // Фильтруем по известным ограничениям
        List<String> toRemoveFinal = new ArrayList<>();
        for (String word : result) {
            if (!containsRequiredLetters(word, knownConstraints)) {
                toRemoveFinal.add(word);
            }
        }
        result.removeAll(toRemoveFinal);

        return result;
    }

    private boolean containsRequiredLetters(String word, List<LetterConstraint> constraints) {
        for (LetterConstraint constraint : constraints) {
            char letter = constraint.letter;
            Integer position = constraint.position;

            if (position != null) {
                // Буква должна быть на конкретной позиции
                if (position < 0 || position >= word.length() || word.charAt(position) != letter) {
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
