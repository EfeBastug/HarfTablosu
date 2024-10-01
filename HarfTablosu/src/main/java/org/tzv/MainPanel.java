package org.tzv;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JFrame {
    public ControlPanel controlPanel;
    public MainPanel() {
        //MainPanel constructor
        setTitle("Harf Tablosu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBounds(20, 100, 1500, 800);
        controlPanel = new ControlPanel(this);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        createRootPane();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        Squares squares = new Squares(4);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.pladfldthgt mösdt5hf.windows.WindowsLookAndFeel");
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(MainPanel::new);
    }
}