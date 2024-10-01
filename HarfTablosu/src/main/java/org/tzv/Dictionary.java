package org.tzv;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dictionary {
    private final Map<Character, ArrayList<String>> dict2 = new HashMap<>();
    private final File wordsFile = new File("HarfTablosu/src/main/resources/4harfli.txt");

    public Dictionary() {
        initializeDictionaries();
    }

    private void initializeDictionaries() {
        try (BufferedReader reader = new BufferedReader(new FileReader(wordsFile))) {
            String word;
            while ((word = reader.readLine()) != null) {
                char firstLetter = word.charAt(0);
                dict2.computeIfAbsent(firstLetter, k -> new ArrayList<>()).add(word);
            }
        } catch (IOException e) {
            System.out.println("Sözlük dosyası doğru yüklenemedi.");
        }
    }

    public void printLetter(char letter) {
        ArrayList<String> wordsList = dict2.get(letter);
        if (wordsList != null) {
            wordsList.forEach(System.out::println);
        } else {
            System.out.println("No words found for letter: " + letter);
        }
    }

}
