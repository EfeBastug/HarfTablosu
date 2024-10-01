package org.tzv;
import javax.swing.*;
import java.awt.*;

public class QuestionPanel extends JPanel implements Cloneable {
    public Squares question;
    MainPanel mainPanel;
    public boolean draw = true;
    boolean saved = true;
    int cellSize;
    private final double PRINT_SCALING_FACTOR = 0.5;
    Font font = null;
    boolean print = false;
    int size = 4;
    int board_length = size * 50; // Hücre boyutu
    int middle_space = 20;
    int left_space = 50;
    int up_space = 50;
    int right_board = left_space + board_length + middle_space;
    Color tablo = Color.DARK_GRAY,
            tabloIc = Color.WHITE,
            cerceve = Color.BLACK,
            disipucu = Color.WHITE,
            disarka = Color.WHITE,
            cevap = Color.BLACK,
            arkaPlan = Color.WHITE,
            icipucu = Color.BLACK,
            hints = Color.BLACK,
            c1 = Color.BLUE, c2 = Color.RED, c3 = Color.YELLOW;

    public QuestionPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.question = new Squares(4); // 4x4 board and 8 colors
        setBackground(Color.WHITE);
        cellSize = Math.min(this.getWidth(), this.getHeight()) / (size + 2);
        setLayout(null);
    }


    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("I was here 1!");
        super.paintComponent(g);
        if (draw) {
            int componentWidth = print ? (int) (getWidth() * PRINT_SCALING_FACTOR) : getWidth();
            int componentHeight = print ? (int) (getHeight() * PRINT_SCALING_FACTOR) : getHeight();
            int cellSize = Math.min(componentWidth, componentHeight) / (size + 2);
            board_length = size * cellSize;
            left_space = (int) ((componentWidth - board_length) / ((2 * size) - 1.4));
            up_space = (int) ((componentHeight - board_length) / 2.5);
            drawGridAndLetters(g, question, cellSize, left_space - 40, up_space);
        }
    }

    public void drawGridAndLetters(Graphics g, Squares squares, int cellSize, int leftSpace, int upSpace) {
        System.out.println("I was here 2!");
        int size = 4;
        int boardLength = size * cellSize;

        // Hücreleri doldur
        g.setColor(tabloIc);
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = leftSpace + col * cellSize;
                int y = upSpace + row * cellSize;
                g.fillRect(x, y, cellSize, cellSize);
            }
        }

        // Grid çizgilerini çiz
        g.setColor(tablo);
        for (int i = 0; i <= size; i++) {
            g.drawLine(leftSpace, upSpace + i * cellSize, leftSpace + boardLength, upSpace + i * cellSize);
            g.drawLine(leftSpace + i * cellSize, upSpace, leftSpace + i * cellSize, upSpace + boardLength);
        }

        // Hücrelerin içine harfleri yaz
        g.setColor(Color.BLACK); // Harflerin rengi
        Font font = new Font("Arial", Font.PLAIN, cellSize / 2); // Font ayarları
        g.setFont(font);

        FontMetrics metrics = g.getFontMetrics(font);
        int fontHeight = metrics.getHeight();
        int fontAscent = metrics.getAscent();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = leftSpace + col * cellSize;
                int y = upSpace + row * cellSize;

                // Mevcut hücre için harfi al
                String letter = squares.board.get(row).get(col);


                // Harfi ortalamak için x ve y koordinatlarını hesapla
                int letterX = x + (cellSize - metrics.stringWidth(letter)) / 2;
                int letterY = y + (cellSize - fontHeight) / 2 + fontAscent;

                // Harfi çiz
                g.drawString(letter, letterX, letterY);
            }
        }

        // Eğer varsa, diğer öğeleri çiz
        squares.drawHeaders(g, cellSize, leftSpace, upSpace);
    }

    @Override
    public QuestionPanel clone() {
        try {
            QuestionPanel clone = (QuestionPanel) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            // TODO: Sure, why not
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
