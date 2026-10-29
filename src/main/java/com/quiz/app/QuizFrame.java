package com.quiz.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class QuizFrame extends JFrame {

    private final String studentName;
    private final List<Question> questions;

    private int currentQuestion = 0;
    private int score = 0;

    private final int[] selectedAnswers;
    private final JLabel questionNumberLabel = new JLabel();
    private final JLabel questionLabel = new JLabel();
    private final JRadioButton[] optionButtons = new JRadioButton[4];
    private final ButtonGroup optionGroup = new ButtonGroup();

    private final Color NAVY = new Color(25, 35, 75);
    private final Color PURPLE = new Color(105, 75, 220);
    private final Color BG = new Color(245, 247, 255);

    public QuizFrame(String studentName) {
        this.studentName = studentName;
        this.questions = DatabaseManager.getQuestions();
        this.selectedAnswers = new int[questions.size()];

        for (int i = 0; i < selectedAnswers.length; i++) {
            selectedAnswers[i] = -1;
        }

        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No questions are available in the database.",
                    "Quiz Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
            dispose();
            return;
        }

        buildUI();
        showQuestion();
    }

    private void buildUI() {
        setTitle("Quiz Management System");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Knowledge Quiz");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(NAVY);

        JLabel welcome = new JLabel("Student: " + studentName);
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 15));
        welcome.setForeground(new Color(90, 95, 120));

        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        // Question card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        questionNumberLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        questionNumberLabel.setForeground(PURPLE);
        questionNumberLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 21));
        questionLabel.setForeground(NAVY);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(questionNumberLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(questionLabel);
        card.add(Box.createVerticalStrut(25));

        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 16));
            optionButtons[i].setBackground(Color.WHITE);
            optionButtons[i].setForeground(NAVY);
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            optionGroup.add(optionButtons[i]);

            card.add(optionButtons[i]);
            card.add(Box.createVerticalStrut(14));
        }

        // Navigation buttons
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JButton exitButton = new JButton("Exit Quiz");
        styleButton(exitButton, new Color(225, 228, 240), NAVY);
        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit the quiz?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new Main().showHome();
            }
        });

        JButton nextButton = new JButton("Next Question  →");
        styleButton(nextButton, PURPLE, Color.WHITE);
        nextButton.addActionListener(e -> goToNextQuestion());

        footer.add(exitButton, BorderLayout.WEST);
        footer.add(nextButton, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void showQuestion() {
        Question q = questions.get(currentQuestion);

        questionNumberLabel.setText(
                "QUESTION " + (currentQuestion + 1) + " OF " + questions.size()
        );

        questionLabel.setText(
                "<html><div style='width:650px'>"
                        + q.getQuestionText()
                        + "</div></html>"
        );

        String[] options = q.getOptions();
        optionGroup.clearSelection();

        for (int i = 0; i < optionButtons.length; i++) {
            if (i < options.length) {
                optionButtons[i].setText(
                        (char) ('A' + i) + ".  " + options[i]
                );
                optionButtons[i].setVisible(true);
                optionButtons[i].setSelected(
                        selectedAnswers[currentQuestion] == i
                );
            } else {
                optionButtons[i].setVisible(false);
            }
        }
    }

    private void goToNextQuestion() {
        int selected = getSelectedOption();

        if (selected == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an answer before continuing.",
                    "Answer Required",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        selectedAnswers[currentQuestion] = selected;

        if (currentQuestion < questions.size() - 1) {
            currentQuestion++;
            showQuestion();
        } else {
            submitQuiz();
        }
    }

    private int getSelectedOption() {
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isVisible()
                    && optionButtons[i].isSelected()) {
                return i;
            }
        }
        return -1;
    }

    private void submitQuiz() {
        score = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (selectedAnswers[i] == questions.get(i).getCorrectAnswer()) {
                score++;
            }
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "You have answered all questions.\nSubmit your quiz?",
                "Submit Quiz",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        DatabaseManager.saveResult(
                studentName,
                score,
                questions.size()
        );

        dispose();
        new ResultFrame(
                studentName,
                score,
                questions.size(),
                questions,
                selectedAnswers
        ).setVisible(true);
    }

    private void styleButton(
            JButton button, Color background, Color foreground) {

        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
