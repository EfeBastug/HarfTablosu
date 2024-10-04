package org.tzv;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Solution {

    private int size;
    private ArrayList<ArrayList<String>> answerBoard;
    private ArrayList<ArrayList<String>> questionBoard;
    private ArrayList<ArrayList<String>> tempBoard;

    private ArrayList<String> solutionStr; // Tüm çözümleri saklamak için
    private ArrayList<String> allSolutions; // Bulunan tüm çözümleri saklamak için

    private String[] turkishAlphabet = {"a", "b", "c", "ç", "d", "e", "f", "g", "ğ", "h", "ı", "i", "j", "k", "l", "m", "n", "o", "ö", "p", "r", "s", "ş", "t", "u", "ü", "v", "y", "z"};
    private int matchingSolutionCount = 0;

    public Solution(int size, ArrayList<ArrayList<String>> question, ArrayList<ArrayList<String>> answer) {
        this.size = size;
        this.answerBoard = answer;
        this.questionBoard = question;
        this.solutionStr = new ArrayList<>(); // Initialize here
        this.allSolutions = new ArrayList<>(); // Initialize the list to hold all solutions
    }
    // Deep copy method to copy a board
    private ArrayList<ArrayList<String>> deepCopyBoard(ArrayList<ArrayList<String>> board) {
        ArrayList<ArrayList<String>> copy = new ArrayList<>();
        for (ArrayList<String> row : board) {
            copy.add(new ArrayList<>(row)); // Deep copy of each row
        }
        return copy;
    }

    // Write the board and operations to a text file
    private void writeBoardToTxt(FileWriter writer, ArrayList<ArrayList<String>> tempAnswer, String operations) throws IOException {
        writer.write("Original Question Board:\n");
        for (ArrayList<String> row : questionBoard) {
            StringBuilder rowString = new StringBuilder();
            for (String element : row) {
                rowString.append(element).append(" ");
            }
            writer.write(rowString.toString().trim() + "\n");
        }

        writer.write("\nOperations Applied: " + operations + "\n");

        writer.write("Transformed Board:\n");
        for (ArrayList<String> row : tempAnswer) {
            StringBuilder rowString = new StringBuilder();
            for (String element : row) {
                rowString.append(element).append(" ");
            }
            writer.write(rowString.toString().trim() + "\n");
        }

        writer.write("\n------------------------------------------\n");
    }

    // Shift a row to the right
    private void shiftRowRight(ArrayList<ArrayList<String>> tempQuestion, int row) {
        ArrayList<String> currentRow = tempQuestion.get(row);
        String lastElement = currentRow.get(currentRow.size() - 1);
        for (int i = currentRow.size() - 1; i > 0; i--) {
            currentRow.set(i, currentRow.get(i - 1));
        }
        currentRow.set(0, lastElement);
    }

    // Shift letters in a column to the previous Turkish letter
    private void shiftColumnLetters(ArrayList<ArrayList<String>> tempQuestion, int colIndex) {
        for (int row = 0; row < size; row++) {
            String letter = tempQuestion.get(row).get(colIndex);
            String newLetter = getNextTurkishLetter(letter);
            tempQuestion.get(row).set(colIndex, newLetter);
        }
    }

    // Get the previous Turkish letter
    // Get the next Turkish letter
    private String getNextTurkishLetter(String letter) {
        if (letter.isEmpty()) {
            return letter; // If empty, return as is
        }

        int index = Arrays.asList(turkishAlphabet).indexOf(letter);
        if (index == -1) {
            return letter; // Return as is if not found
        } else if (index == turkishAlphabet.length - 1) {
            return turkishAlphabet[0]; // Return first letter if last
        } else {
            return turkishAlphabet[index + 1]; // Return next letter
        }
    }


    // Compare two boards
    private boolean compareBoards(ArrayList<ArrayList<String>> tempQuestion, ArrayList<ArrayList<String>> tempAnswer) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!tempQuestion.get(i).get(j).equals(tempAnswer.get(i).get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    // Generate all combinations of operations
    public void bruteForceAllCombinations() {
        try (FileWriter writer = new FileWriter("s.txt")) {
            writer.write("Solutions and Operations:\n\n");

            // Generate operations
            ArrayList<String> operations = generateOperations();

            // Try combinations of different lengths
            for (int r = 1; r <= operations.size(); r++) {
                ArrayList<ArrayList<String>> combinations = generateCombinations(operations, r);

                for (ArrayList<String> combination : combinations) {
                    ArrayList<ArrayList<String>> permutations = generatePermutations(combination);

                    for (ArrayList<String> permutation : permutations) {
                        tempBoard = deepCopyBoard(questionBoard); // Create new copy

                        // Apply each operation in the permutation
                        for (String operation : permutation) {
                            applyOperation(tempBoard, operation);
                        }

                        // Write result to file
                        writeBoardToTxt(writer, tempBoard, String.join(", ", permutation));
                        if (compareBoards(tempBoard, answerBoard)) {
                            matchingSolutionCount++;
                            solutionStr.add(String.valueOf(permutation));
                            allSolutions.add(String.valueOf(permutation)); // Store each valid solution
                        }
                    }
                }
            }

            writer.write("Total matching solutions: " + matchingSolutionCount + "\n");
            System.out.println("Total matching solutions: " + matchingSolutionCount);
            System.out.println("Found Solutions: " + allSolutions); // Print all found solutions

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    // Apply an operation to the board
    private void applyOperation(ArrayList<ArrayList<String>> tempQuestion, String operation) {
        if (operation.startsWith("C")) {
            int colIndex = Integer.parseInt(operation.substring(1));
            shiftColumnLetters(tempQuestion, colIndex);
        } else if (operation.startsWith("R")) {
            int rowIndex = Integer.parseInt(operation.substring(1));
            shiftRowRight(tempQuestion, rowIndex);
        }
    }

    // Generate operations
    private ArrayList<String> generateOperations() {
        ArrayList<String> operations = new ArrayList<>();

        // Add column shifts (C0, C1, ..., Cn)
        for (int i = 0; i < size; i++) {
            operations.add("C" + i);
        }

        // Add row shifts (R0, R1, ..., Rn)
        for (int i = 0; i < size; i++) {
            operations.add("R" + i);
        }

        return operations;
    }

    // Generate combinations of operations
    private ArrayList<ArrayList<String>> generateCombinations(ArrayList<String> operations, int r) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        combine(operations, new ArrayList<>(), r, 0, result);
        return result;
    }

    // Combination function
    private void combine(ArrayList<String> operations, ArrayList<String> current, int r, int start, ArrayList<ArrayList<String>> result) {
        if (current.size() == r) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < operations.size(); i++) {
            current.add(operations.get(i));
            combine(operations, current, r, i + 1, result);
            current.remove(current.size() - 1);
        }
    }

    // Generate permutations of operations
    private ArrayList<ArrayList<String>> generatePermutations(ArrayList<String> operations) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        permute(operations, 0, result);
        return result;
    }

    // Permutation generation function
    private void permute(ArrayList<String> list, int k, ArrayList<ArrayList<String>> result) {
        for (int i = k; i < list.size(); i++) {
            java.util.Collections.swap(list, i, k);
            permute(list, k + 1, result);
            java.util.Collections.swap(list, k, i);
        }
        if (k == list.size() - 1) {
            result.add(new ArrayList<>(list));
        }
    }





}