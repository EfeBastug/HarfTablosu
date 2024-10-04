package org.tzv;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class QuestionPanel extends JPanel implements Cloneable {

    public Squares answer;
    public Squares question;
    MainPanel mainPanel;

    public QuestionPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.answer = new Squares(4); // Create a 4x4 grid in the 'answer' object
        this.question = new Squares(4);
        setBackground(Color.WHITE);
        setLayout(null);

        deepCopyBoard(this.answer, this.question);
        this.question.performRandomOperations(5);

        System.out.println("question: " + this.question.board);
        System.out.println("solution: "+ this.answer.board);
        Solution sol = new Solution(question.size, this.question.board, this.answer.board);
        sol.bruteForceAllCombinations();

    }

    public void deepCopyBoard(Squares source, Squares target) {
        ArrayList<ArrayList<String>> newBoard = new ArrayList<>();
        for (ArrayList<String> row : source.board) {
            newBoard.add(new ArrayList<>(row)); // Creating a new ArrayList for each row
        }
        target.board = newBoard; // Assigning the deep-copied board to the target
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Define the cell size and spacing for the board
        int cellSize = 60;
        int leftSpace = 50; // Space from the left side of the panel
        int upSpace = 50;   // Space from the top of the panel

        // Draw the grid and the content of the 'answer' board
        question.drawTable(g, cellSize, leftSpace, upSpace);

        answer.drawTable(g, cellSize, leftSpace + 500, upSpace);
    }
}
