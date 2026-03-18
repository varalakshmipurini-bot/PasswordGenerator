package com.lakhi.password;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main extends JFrame {

    private JTextField lengthField;
    private JTextField customSpecialField;
    private JTextField excludeCharsField;
    private JTextField countField;
    private JTextField pronounceableBaseField;
    private JTextField passwordField;

    private JTextArea historyArea;
    private JLabel strengthLabel;

    private JCheckBox lowerCheck;
    private JCheckBox upperCheck;
    private JCheckBox numberCheck;
    private JCheckBox specialCheck;
    private JCheckBox avoidSimilarCheck;

    private PasswordGenerator generator;
    private List<String> passwordHistory;
    private JCheckBox noRepeatCheck;

    public Main() {
        generator = new PasswordGenerator();
        passwordHistory = new ArrayList<>();

        setTitle("Advanced Password Generator");
        setSize(750, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel inputPanel = new JPanel(new GridLayout(11, 2, 5, 5));

        inputPanel.add(new JLabel("Password Length:"));
        lengthField = new JTextField("8");
        inputPanel.add(lengthField);

        lowerCheck = new JCheckBox("Include Lowercase", true);
        upperCheck = new JCheckBox("Include Uppercase", true);
        numberCheck = new JCheckBox("Include Numbers", true);
        specialCheck = new JCheckBox("Include Special Characters", true);
        avoidSimilarCheck = new JCheckBox("Avoid Similar Characters", true);
        noRepeatCheck = new JCheckBox("No Repeated Characters");
        inputPanel.add(noRepeatCheck);

        inputPanel.add(lowerCheck);
        inputPanel.add(upperCheck);
        inputPanel.add(numberCheck);
        inputPanel.add(specialCheck);
        inputPanel.add(avoidSimilarCheck);
        inputPanel.add(new JLabel(""));

        inputPanel.add(new JLabel("Custom Special Characters:"));
        customSpecialField = new JTextField("@#$%");
        inputPanel.add(customSpecialField);

        inputPanel.add(new JLabel("Exclude Characters:"));
        excludeCharsField = new JTextField();
        inputPanel.add(excludeCharsField);

        inputPanel.add(new JLabel("Generate Multiple Passwords:"));
        countField = new JTextField("1");
        inputPanel.add(countField);

        inputPanel.add(new JLabel("Pronounceable Base Word:"));
        pronounceableBaseField = new JTextField("Ravi");
        inputPanel.add(pronounceableBaseField);

        add(inputPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));

        passwordField = new JTextField();
        passwordField.setEditable(false);
        centerPanel.add(passwordField, BorderLayout.NORTH);

        strengthLabel = new JLabel("Strength: ");
        centerPanel.add(strengthLabel, BorderLayout.CENTER);

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        centerPanel.add(scrollPane, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton generateButton = new JButton("Generate");
        JButton regenerateButton = new JButton("Regenerate");
        JButton copyButton = new JButton("Copy");
        JButton saveButton = new JButton("Save");
        JButton pronounceableButton = new JButton("Pronounceable");

        buttonPanel.add(generateButton);
        buttonPanel.add(regenerateButton);
        buttonPanel.add(copyButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(pronounceableButton);

        add(buttonPanel, BorderLayout.SOUTH);

        generateButton.addActionListener(e -> generatePasswords());
        regenerateButton.addActionListener(e -> generatePasswords());
        copyButton.addActionListener(e -> copyPassword());
        saveButton.addActionListener(e -> savePasswords());
        pronounceableButton.addActionListener(e -> generatePronounceablePassword());
    }

    private void generatePasswords() {
        try {
            int length = Integer.parseInt(lengthField.getText().trim());
            int count = Integer.parseInt(countField.getText().trim());

            boolean useLower = lowerCheck.isSelected();
            boolean useUpper = upperCheck.isSelected();
            boolean useNumbers = numberCheck.isSelected();
            boolean useSpecial = specialCheck.isSelected();
            boolean avoidSimilar = avoidSimilarCheck.isSelected();
            boolean noRepeat = noRepeatCheck.isSelected();

            String customSpecial = customSpecialField.getText();
            String excludeChars = excludeCharsField.getText();

            List<String> passwords = generator.generateMultiplePasswords(
                    count, length, useLower, useUpper, useNumbers,
                    useSpecial, avoidSimilar, noRepeat, customSpecial, excludeChars
            );

            historyArea.setText("");
            for (String password : passwords) {
                historyArea.append(password + "\n");
                passwordHistory.add(password);
            }

            if (!passwords.isEmpty()) {
                passwordField.setText(passwords.get(0));

                double entropy = generator.calculateEntropy(
                        passwords.get(0),
                        useLower,
                        useUpper,
                        useNumbers,
                        useSpecial,
                        customSpecial,
                        avoidSimilar,
                        excludeChars
                );

                strengthLabel.setText("Strength: " + generator.checkPasswordStrength(passwords.get(0))
                        + " | Entropy: " + String.format("%.2f", entropy) + " bits");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
        }
    }
    private void generatePronounceablePassword() {
        String baseWord = pronounceableBaseField.getText().trim();
        String password = generator.generatePronounceablePassword(baseWord, true, true);

        passwordField.setText(password);
        strengthLabel.setText("Strength: " + generator.checkPasswordStrength(password));
        historyArea.append(password + "\n");
        passwordHistory.add(password);
    }

    private void copyPassword() {
        String password = passwordField.getText();

        if (password == null || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No password to copy.");
            return;
        }

        StringSelection selection = new StringSelection(password);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Password copied!");
    }

    private void savePasswords() {
        if (passwordHistory.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No passwords to save.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            String[] formats = {"Text File (.txt)", "CSV File (.csv)"};
            JComboBox<String> formatBox = new JComboBox<>(formats);

            int choice = JOptionPane.showConfirmDialog(this, formatBox, "Choose Save Format",
                    JOptionPane.OK_CANCEL_OPTION);

            if (choice == JOptionPane.OK_OPTION) {
                try {
                    if (formatBox.getSelectedIndex() == 0) {
                        if (!filePath.endsWith(".txt")) {
                            filePath += ".txt";
                        }
                        FileUtils.saveToTextFile(filePath, passwordHistory);
                    } else {
                        if (!filePath.endsWith(".csv")) {
                            filePath += ".csv";
                        }
                        FileUtils.saveToCSVFile(filePath, passwordHistory);
                    }

                    JOptionPane.showMessageDialog(this, "Passwords saved successfully!");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}