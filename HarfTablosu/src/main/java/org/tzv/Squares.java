package org.tzv;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Squares extends JPanel implements Cloneable {
    protected int size;
    public Dictionary dictionary;
    public ArrayList<ArrayList<String>> board;
    private Random random;

    private String questionString;

    String[] turkishAlphabet = {"A", "B", "C", "Ç", "D", "E", "F", "G", "Ğ", "H", "I", "İ", "J", "K", "L", "M",
            "N", "O", "Ö", "P", "R", "S", "Ş", "T", "U", "Ü", "V", "Y", "Z"};

    public Squares(int size) {
        this.size = size;
        this.dictionary = new Dictionary();
        this.board = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            board.add(new ArrayList<>(Collections.nCopies(size, "")));
        }
        this.random = new Random();
        fillBoard();
        /*for(int i=0;i<size;i++) {
            System.out.println(board.get(i));
        }*/
    }

    private void fillBoard() {
        if (!backtrack(0)) {
            System.out.println("No solution found.");
        }
    }

    // Backtracking function to fill the board row by row
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
        for (Character c : dictionary.dict2.keySet()) {
            ArrayList<String> wordsForLetter = dictionary.dict2.get(c);
            for (String word : wordsForLetter) {
                if (word.length() == length) {
                    wordsOfLength.add(word);
                }
            }
        }
        Collections.shuffle(wordsOfLength); // Shuffle the list to ensure randomness
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
    public void performRandomOperations(int operationsCount) {
        Set<Integer> usedRows = new HashSet<>();
        Set<Integer> usedCols = new HashSet<>();
        Random random = new Random();

        for (int i = 0; i < operationsCount; i++) {
            // Randomly choose to operate on a row or a column
            boolean operateOnRow = random.nextBoolean();
            int index;

            if (operateOnRow) {
                // Select a unique row
                do {
                    index = random.nextInt(size);
                } while (usedRows.contains(index));
                usedRows.add(index);

                // Shift the selected row to the left
                shiftRowLeft(index);

                // Print row labels: 5 + index for the range 5, 6, 7, 8
                StringBuilder rows = new StringBuilder();
                for (int j = 0; j < size; j++) {
                    rows.append((size + j + 1)).append(" ");
                }
                System.out.println("Shifted row " + (size + index + 1) + " to the left. Affected rows: " + rows.toString().trim());
            } else {
                // Select a unique column
                do {
                    index = random.nextInt(size);
                } while (usedCols.contains(index));
                usedCols.add(index);

                // Shift the selected column to the left
                shiftColumnLeft(index);
                System.out.println("Shifted column " + (index + 1) + " to the top."); // Log the operation
            }
        }
    }

    // ... Existing methods remain unchanged ...

    private void shiftRowLeft(int rowIndex) {
        // Shift all elements to the left in the specified row
        String firstElement = board.get(rowIndex).remove(0); // Remove the first element
        board.get(rowIndex).add(firstElement); // Add it to the end of the row

        // Replace each letter with the previous letter in the Turkish alphabet
        for (int col = 0; col < size; col++) {
            String letter = board.get(rowIndex).get(col);
            String newLetter = getPreviousTurkishLetter(letter);
            board.get(rowIndex).set(col, newLetter);
        }
    }

    private void shiftColumnLeft(int colIndex) {
        // Shift all elements to the top in the specified column
        String firstElement = board.get(0).get(colIndex); // Get the first element
        for (int row = 0; row < size - 1; row++) {
            board.get(row).set(colIndex, board.get(row + 1).get(colIndex)); // Move up the elements
        }
        board.get(size - 1).set(colIndex, firstElement); // Place the first element at the bottom

        // Replace each letter with the previous letter in the Turkish alphabet
        for (int row = 0; row < size; row++) {
            String letter = board.get(row).get(colIndex);
            String newLetter = getPreviousTurkishLetter(letter);
            board.get(row).set(colIndex, newLetter);
        }
    }

    private String getPreviousTurkishLetter(String letter) {
        if (letter.isEmpty()) {
            return letter; // Return empty if there is no letter
        }

        // Find the index of the letter in the Turkish alphabet
        int index = Arrays.asList(turkishAlphabet).indexOf(letter);
        if (index == 0) {
            return turkishAlphabet[turkishAlphabet.length - 1]; // Wrap around to the last letter
        } else if (index > 0) {
            return turkishAlphabet[index - 1]; // Return the previous letter
        }
        return letter; // If the letter is not found, return it as is
    }
    // print content in screen

    public void drawTable(Graphics g, int cellSize, int leftSpace, int upSpace) {
        // Set font for the headers
        g.setFont(g.getFont().deriveFont(24f));
        g.setColor(Color.BLACK);

        // Draw column headers (numbers 1, 2, 3,...)
        char colHeader = '1';
        for (int col = 0; col < size; col++) {
            int x = leftSpace + col * cellSize + cellSize / 2 - 5;
            int y = upSpace - 10; // Positioning above the grid
            g.drawString(String.valueOf(colHeader++), x, y);
        }

        // Draw row headers (letters A, B, C,...)
        char rowHeader = '5';
        for (int row = 0; row < size; row++) {
            int x = leftSpace - 30; // Positioning to the left of the grid
            int y = upSpace + row * cellSize + cellSize / 2 + 5;
            g.drawString(String.valueOf(rowHeader++), x, y);
        }

        // Draw the grid and fill in the letters from the board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Draw each cell
                int x = leftSpace + col * cellSize;
                int y = upSpace + row * cellSize;
                g.drawRect(x, y, cellSize, cellSize);

                // Draw the letter inside the cell (centered)
                String letter = board.get(row).get(col);
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                int letterX = x + (cellSize - metrics.stringWidth(letter)) / 2;
                int letterY = y + ((cellSize - metrics.getHeight()) / 2) + metrics.getAscent();
                g.drawString(letter, letterX, letterY);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = 60; // Size of each cell
        int leftSpace = 40; // Space on the left
        int upSpace = 40; // Space at the top
        drawTable(g, cellSize, leftSpace, upSpace);
    }



}

