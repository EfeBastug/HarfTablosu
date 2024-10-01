package org.tzv;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Squares extends JPanel implements Cloneable {
    public int size;
    public Dictionary dictionary;
    public ArrayList<ArrayList<String>> board;

    public Squares(int size) {
        this.size = size;
        this.dictionary = new Dictionary();
        this.board = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            board.add(new ArrayList<>(Collections.nCopies(size, "")));
        }
        Random random = new Random();
        fillBoard();
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

    public void drawHeaders(Graphics g, int cellSize, int leftSpace, int upSpace) {
        // Set font for the headers
        g.setFont(g.getFont().deriveFont(24f));
        g.setColor(Color.BLACK);

        // Draw column headers
        char colHeader = 'A';
        for (int col = 0; col < size; col++) {
            int x = leftSpace + col * cellSize + cellSize / 2;
            int y = upSpace - 5; // Positioning above the grid
            g.drawString(String.valueOf(colHeader++), x, y);
        }

        // Draw row headers
        char rowHeader = colHeader;
        for (int row = 0; row < size; row++) {
            int x = leftSpace - 20; // Positioning to the left of the grid
            int y = upSpace + row * cellSize + cellSize / 2;
            g.drawString(String.valueOf(rowHeader++), x, y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = 60; // Size of each cell
        int leftSpace = 40; // Space on the left
        int upSpace = 40; // Space at the top
        drawHeaders(g, cellSize, leftSpace, upSpace);
        //drawCircles(g, cellSize, leftSpace, upSpace);
    }

    @Override
    public Squares clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Squares) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    /*public void drawCircles(Graphics g, int cellSize, int leftSpace, int upSpace) {
        int circleDiameter = (int) (cellSize / 1.2); // Adjusted circle diameter

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = leftSpace + col * cellSize + (cellSize - circleDiameter) / 2;
                int y = upSpace + row * cellSize + (cellSize - circleDiameter) / 2;

                // Get color from the board
                int colorIndex = board.get(row).get(col); // Retrieve color index from board
                Color circleColor = getColorByIndex(colorIndex);

                g.setColor(circleColor);
                g.fillOval(x, y, circleDiameter, circleDiameter);

                // Draw border around circle
                g.setColor(Color.BLACK); // Set border color
                g.drawOval(x, y, circleDiameter, circleDiameter); // Draw the border
            }
        }
    }*/
}

