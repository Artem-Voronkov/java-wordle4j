package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;
    private String answer;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>();

        for (String word: words) {
            String lower = toLower(word);
            if (lower.length() == 5) {
                this.words.add(lower);
            }
        }
    }

    private String toLower(String word) {
        return word.toLowerCase().replace('ё', 'e');
    }

    public boolean contains(String word) {
        String normalize = toLower(word);

        for(String dictWord: words) {
            if (dictWord.equals(normalize)) {
                return true;
            }
        }
        return false;
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
        StringBuilder result = new StringBuilder("     ");
        boolean[] usedInAnswer = new boolean[5];

        for (int i = 0; i < 5; i++) {
            if (guessChars[i] == answerChars[i]) {
                result.setCharAt(i, '+');
                usedInAnswer[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (result.charAt(i) != '+') {
                for (int j = 0; j < 5; j++) {
                    if (!usedInAnswer[j] && guessChars[i] == answerChars[j]) {
                        result.setCharAt(i, '^');
                        usedInAnswer[j] = true;
                        break;
                    }
                }

                if (result.charAt(i) == ' ') {
                    result.setCharAt(i, '-');
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
                String actualResult = analyzeMatch(guess, answer);
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
            boolean inCorrectPosition = entry.getValue();

            int letterIndex = word.indexOf(letter);

            if (inCorrectPosition) {
                if (letterIndex == -1) {
                    return false;
                }
            } else {
                if (letterIndex == -1) {
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

    public List<String> getAllWords() {
        return new ArrayList<>(words);
    }

}
