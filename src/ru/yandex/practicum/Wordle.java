package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {
    public static void main(String[] args) {
        try (PrintWriter log = new PrintWriter("wordle.log")) {
            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = loader.loadDictionary("words_ru.txt");
            WordleGame game = new WordleGame(dictionary, log);

            Scanner scanner = new Scanner(System.in);

            System.out.println("Добро пожаловать в Wordle!");
            System.out.println("У вас есть 6 попыток, чтобы угадать слово из 5 букв.");
            System.out.println("Обозначения: + — правильная буква на правильной позиции,");
            System.out.println("^ — правильная буква на неправильной позиции, - — буквы нет в слове");
            System.out.println("Для получения подсказки просто нажмите Enter");

            boolean playAgain;
            do {
                game.resetGame();
                System.out.println("\n--- НОВАЯ ИГРА ---");
                System.out.println("Загадано слово из " + dictionary.size() + " возможных.");

                while (!game.isGameOver()) {
                    System.out.printf("Осталось попыток: %d. Введите слово: ", game.getAttemptsLeft());
                    String input = scanner.nextLine().trim();

                    if (input.isEmpty()) {
                        String hint = game.getHint();
                        System.out.println("Подсказка: " + hint);
                    } else {
                        // Валидация ввода
                        if (input.length() != 5) {
                            System.out.println("Слово должно содержать ровно 5 букв!");
                            continue;
                        }
                        if (!input.matches("[а-яё]+")) {
                            System.out.println("Используйте только русские буквы!");
                            continue;
                        }

                        try {
                            String result = game.makeGuess(input);
                            System.out.println(input);
                            System.out.println(result);
                        } catch (WordNotFoundInDictionary e) {
                            System.out.println("Слово '" + input + "' не найдено в словаре. Попробуйте ещё раз.");
                        }
                    }
                }

                if (game.hasWon()) {
                    System.out.println("Поздравляем! Вы угадали слово '" + game.getAnswer() + "'!");
                } else {
                    System.out.println("Игра окончена. Загаданное слово: '" + game.getAnswer() + "'");
                }

                System.out.print("Хотите сыграть ещё раз? (да/нет): ");
                playAgain = scanner.nextLine().trim().toLowerCase().equals("да");
            } while (playAgain);

            System.out.println("Спасибо за игру!");
        } catch (DictionaryLoadException e) {
            System.err.println("Ошибка загрузки словаря: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
        }
    }
}
