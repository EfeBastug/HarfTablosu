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

    private String[] turkishAlphabet = {"a", "b", "c", "ç", "d", "e", "f", "g", "ğ", "h", "ı", "i", "j", "k", "l", "m", "n", "o", "ö", "p", "r", "s", "ş", "t", "u", "ü", "v", "y", "z"};


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
            // Rastgele olarak satır ya da sütun üzerinde işlem yap
            boolean operateOnRow = random.nextBoolean();
            int index;

            if (operateOnRow) {
                // Eşsiz bir satır seç
                do {
                    index = random.nextInt(size);
                } while (usedRows.contains(index));
                usedRows.add(index);

                // Seçilen satırı sola kaydır
                shiftRowLeft(index);

                // Kaydırılan satır hakkında bilgi yazdır (satır numarasını 1'den başlatıyoruz)
                System.out.println("Shifted row " + (index + 1 + size) + " to the left.");
            } else {
                // Eşsiz bir sütun seç
                do {
                    index = random.nextInt(size);
                } while (usedCols.contains(index));
                usedCols.add(index);

                // Sütundaki harfleri bir önceki Türk harfine çevir
                shiftColumnLetters(index);

                // Sütun numarasını 1'den başlatıyoruz
                System.out.println("Changed letters in column " + (index + 1) + " to the previous Turkish letter.");
            }
        }
    }
    // Satırları sola kaydıran fonksiyon (harf değiştirme yok)
    private void shiftRowLeft(int rowIndex) {
        // Satırdaki tüm elemanları sola kaydır
        String firstElement = board.get(rowIndex).remove(0); // İlk elemanı çıkar
        board.get(rowIndex).add(firstElement); // Çıkarılan elemanı sona ekle
    }

    // Sütundaki harfleri bir önceki Türk harfiyle değiştiren fonksiyon
    private void shiftColumnLetters(int colIndex) {
        // Sütundaki her bir hücreyi ziyaret et ve bir önceki Türk harfine çevir
        for (int row = 0; row < size; row++) {
            String letter = board.get(row).get(colIndex);
            String newLetter = getPreviousTurkishLetter(letter);
            board.get(row).set(colIndex, newLetter);
        }
    }

    // Harfi bir önceki Türk harfiyle değiştiren fonksiyon
    private String getPreviousTurkishLetter(String letter) {
        if (letter.isEmpty()) {
            return letter; // Boş ise aynen döndür
        }

        // Türk alfabesindeki harfin indeksini bul
        int index = Arrays.asList(turkishAlphabet).indexOf(letter);
        if (index == 0) {
            return turkishAlphabet[turkishAlphabet.length - 1]; // İlk harfse son harfe dön
        } else if (index > 0) {
            return turkishAlphabet[index - 1]; // Bir önceki harfi döndür
        }
        return letter; // Harf bulunmazsa aynen döndür
    }

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
                g.drawString(letter.toUpperCase(), letterX, letterY);
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

