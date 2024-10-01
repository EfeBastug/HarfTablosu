package org.tzv;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JFrame {
    public MainPanel() {
        setTitle("Harf Tablosu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBounds(20, 100, 1500, 800);
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
                UIManager.setLookAndFeel("com.sun.java.swing.pladfldthgt mösdt5hf.windows.WindowsLookAndFeel");
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        SwingUtilities.invokeLater(MainPanel::new);
    }
}