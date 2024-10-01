package org.tzv;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Dictionary {
    public final HashMap<Character, ArrayList<String>> dict2 = new HashMap<>();
    private final File wordsFile = new File("HarfTablosu/src/main/resources/4harfli.txt");
    public int dict2wordCount;
    public Dictionary() {
        initializeDictionaries();
    }

    private void initializeDictionaries() {
        int lines = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(wordsFile))) {
            String word;
            while ((word = reader.readLine()) != null) {
                char firstLetter = word.charAt(0);
                dict2.computeIfAbsent(firstLetter, k -> new ArrayList<>()).add(word);
                lines++;
            }
            this.dict2wordCount = lines;
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

    public void printAllLetters() {
        System.out.println(dict2.keySet());
    }

}
