package org.tzv;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class Squares extends JPanel implements Cloneable {
    private int size;
    private Dictionary dictionary;
    public ArrayList<ArrayList<String>> board;
    private Random random;

    public Squares(int size) {
        this.size = size;
        this.board = new ArrayList<>();
        this.random = new Random();
        initializeBoard();

    }

    private void initializeBoard() {
        //Default size 4,
        char temp = (char) (random.nextInt(0, 26));
        //The number of words that are in the list for that character
        int wordCount = dictionary.dict2.get(temp).size();
        //We get a random word number for that character
        int wordNumber = (random.nextInt(0, wordCount));
        String word = dictionary.dict2.get(temp).get(wordNumber);
        for(int i=0;i<size;i++) {
            board.get(0).set(i, String.valueOf(word.charAt(i)));
        }
        for(int i=0;i<size;i++) {
            char startingLetter = word.charAt(i);
            wordCount = dictionary.dict2.get(temp).size();
            wordNumber = (random.nextInt(0, wordCount));

        }

    }


    @Override
    public Squares clone() {
        try {
            return (Squares) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
