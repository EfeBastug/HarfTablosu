package org.tzv;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainPanel extends JFrame {
    public ControlPanel controlPanel;
    public QuestionPanel questionPanel;

    public MainPanel() {
        //MainPanel constructor
        setTitle("Harf Tablosu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBounds(20, 100, 1500, 800);
        controlPanel = new ControlPanel(this);
        questionPanel = new QuestionPanel(this);

        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        getContentPane().add(questionPanel, BorderLayout.CENTER);

        createRootPane();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.platform.windows.WindowsLookAndFeel");
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(MainPanel::new);
    }
}