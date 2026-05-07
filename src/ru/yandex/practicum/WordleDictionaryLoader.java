package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary loadDictionary(String filename) throws DictionaryLoadException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new DictionaryLoadException("Файл словаря '" + filename + "' не найден", null);
        }

        List<String> words = new ArrayList<>();


        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    words.add(trimmed);
                }
            }
        } catch (IOException e) {
            throw new DictionaryLoadException(
                    "Ошибка чтения файла '" + filename + "': " + e.getMessage(), e);
        }

        log.println("Загружено " + words.size() + " строк из файла '" + filename + "'");

        if (words.isEmpty()) {
            throw new RuntimeException("Словарь пуст");
        }

        return new WordleDictionary(words);
    }
}
