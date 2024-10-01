package org.tzv;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Squares extends JPanel implements Cloneable {
    private int size;
    public Dictionary dictionary;
    public ArrayList<ArrayList<String>> board;
    private Random random;

    public Squares(int size) {
        this.size = size;
        this.dictionary = new Dictionary();
        this.board = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            board.add(new ArrayList<>(Collections.nCopies(size, "")));
        }
        this.random = new Random();
        fillBoard();
        for(int i=0;i<size;i++) {
            System.out.println(board.get(i));
        }
    }

    private void fillBoard() {
        if (!backtrack(0)) {
            System.out.println("No solution found.");
        }
    }

    // Backtracking function to fill the board row by row
    private boolean backtrack(int row) {
        // If we've filled all rows, we're done
        if (row == size) {
            return true;
        }

        // Try every word of the correct length for the current row
        for (String word : getWordsOfLength(size)) {
            // Place the word in the current row
            for (int i = 0; i < size; i++) {
                board.get(row).set(i, String.valueOf(word.charAt(i)));
            }

            // Check if the board is still valid after placing the word
            if (isValidBoard(row)) {
                // Recursively attempt to fill the next row
                if (backtrack(row + 1)) {
                    return true;
                }
            }

            // Undo the current placement (backtracking)
            for (int i = 0; i < size; i++) {
                board.get(row).set(i, "");
            }
        }

        // No valid word could be placed in this row
        return false;
    }

    // Check if the board is valid up to the current row
    private boolean isValidBoard(int currentRow) {
        // We only need to check the columns since the rows are already valid
        for (int col = 0; col < size; col++) {
            StringBuilder wordBuilder = new StringBuilder();
            for (int row = 0; row <= currentRow; row++) {
                wordBuilder.append(board.get(row).get(col));
            }
            String partialWord = wordBuilder.toString();

            // Check if there's any word in the dictionary that starts with this prefix
            if (!hasWordWithPrefix(partialWord)) {
                return false;
            }
        }

        return true;
    }


    private ArrayList<String> getWordsOfLength(int length) {
        ArrayList<String> wordsOfLength = new ArrayList<>();
        for (Character c: dictionary.dict2.keySet()) {
            ArrayList<String> wordsForLetter = dictionary.dict2.get(c);
            for (String word : wordsForLetter) {
                if (word.length() == length) {
                    wordsOfLength.add(word);
                }
            }
        }
        return wordsOfLength;
    }

    private boolean hasWordWithPrefix(String prefix) {
        char firstLetter = prefix.charAt(0);
        ArrayList<String> wordsForLetter = dictionary.dict2.get(firstLetter);

        for (String word : wordsForLetter) {
            if (word.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}

