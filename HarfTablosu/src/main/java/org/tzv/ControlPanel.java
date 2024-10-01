package org.tzv;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.itextpdf.text.DocumentException;

public class ControlPanel extends JPanel implements ActionListener, IPrintable {
    MainPanel mainPanel;
    JPanel buttonPanel;
    JComboBox<String> sizeSelectButton;
    JComboBox<String> copyOptionsButton;
    JButton printThisQuestionButton;
    JButton settings, copy, exit, print, rules;
    JSpinner startIndex, endIndex;
    HashSet<String> uniqueQuestions = new HashSet<>();
    HashSet<String> numberedQuestions = new HashSet<>();
    final String[] tableDimensions = {"3", "4", "5", "6"};
    final String[] copyOptions = { "Yan Yana", "Alt Alta", "Soru", "Cevap" };

    public ControlPanel( MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        setLayout(new BorderLayout());
        buttonPanel = new JPanel(new GridLayout(2, 3));
        uniqueQuestions = new HashSet<>();
        numberedQuestions = new HashSet<>();
        createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    void createButtonPanel() {
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startIndex = new JSpinner();
        JComponent startSpinnerEditor = startIndex.getEditor();
        JFormattedTextField jftf = ((JSpinner.DefaultEditor) startSpinnerEditor).getTextField();
        jftf.setColumns(3);

        endIndex = new JSpinner();
        JComponent endSpinnerEditor = endIndex.getEditor();
        JFormattedTextField jftfend = ((JSpinner.DefaultEditor) endSpinnerEditor).getTextField();
        jftfend.setColumns(3);

        JLabel labelDim = new JLabel("Boyut:");
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.add(labelDim);

        sizeSelectButton = new JComboBox<>(tableDimensions);
        sizeSelectButton.setPreferredSize(new Dimension(100, 30));
        sizeSelectButton.addActionListener(this);
        sizeSelectButton.setSelectedIndex(1);
        comboPanel.add(sizeSelectButton);
        buttonPanel.add(comboPanel);

        JPanel comboPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label1 = new JLabel("Boyut:");
        comboPanel1.add(label1);
        buttonPanel.add(new JLabel("İlk :"));
        buttonPanel.add(startIndex);
        buttonPanel.add(new JLabel("Son :"));
        buttonPanel.add(endIndex);

        print = new JButton("Soru Oluştur");
        print.addActionListener((ActionEvent event) -> {
            print.setText("...");
            new Thread(() -> {
                try {
                    buildPDF(this);
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                } finally {
                    print.setText("Soru Oluştur");
                }
            }).start();
        });
        buttonPanel.add(print);

        settings = new JButton("Ayarlar");
        settings.addActionListener(this);
        buttonPanel.add(settings);

        copyOptionsButton = new JComboBox(copyOptions);
        buttonPanel.add(copyOptionsButton);

        copy = new JButton("Kopyala");
        copy.addActionListener(this);
        buttonPanel.add(copy);


        printThisQuestionButton = new JButton("Bu Soruyu Yazdır");
        printThisQuestionButton.addActionListener(this);
        buttonPanel.add(printThisQuestionButton);

        rules = new JButton("Kurallar");
        rules.addActionListener(this);
        buttonPanel.add(rules);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void buildPDF(ControlPanel controlPanel) throws FileNotFoundException, DocumentException{
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public ArrayList getQuestionImage() {
        return null;
    }


}
