//package com.quiz.app;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//
//public class ResultFrame extends JFrame {
//
//    public ResultFrame(String studentName, int score, int total) {
//        setTitle("Quiz Results");
//        setSize(650, 450);
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setResizable(false);
//
//        Color navy = new Color(25, 35, 75);
//        Color purple = new Color(105, 75, 220);
//        Color background = new Color(245, 247, 255);
//
//        JPanel panel = new JPanel();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBackground(background);
//        panel.setBorder(new EmptyBorder(35, 40, 35, 40));
//
//        JLabel heading = new JLabel("Quiz Completed!");
//        heading.setFont(new Font("SansSerif", Font.BOLD, 30));
//        heading.setForeground(navy);
//        heading.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JLabel name = new JLabel("Well done, " + studentName + "!");
//        name.setFont(new Font("SansSerif", Font.PLAIN, 18));
//        name.setForeground(new Color(90, 95, 120));
//        name.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JLabel scoreLabel = new JLabel(score + " / " + total);
//        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 52));
//        scoreLabel.setForeground(purple);
//        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        double percentage = total > 0 ? (score * 100.0 / total) : 0;
//
//        JLabel percentageLabel = new JLabel(
//                String.format("Your score: %.1f%%", percentage)
//        );
//        percentageLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
//        percentageLabel.setForeground(navy);
//        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        String message;
//        if (percentage >= 80) {
//            message = "Excellent performance!";
//        } else if (percentage >= 60) {
//            message = "Good work! Keep improving.";
//        } else {
//            message = "Keep practising. You can do better!";
//        }
//
//        JLabel feedback = new JLabel(message);
//        feedback.setFont(new Font("SansSerif", Font.PLAIN, 16));
//        feedback.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        JButton homeButton = new JButton("Back to Home");
//        homeButton.setFont(new Font("SansSerif", Font.BOLD, 15));
//        homeButton.setForeground(Color.WHITE);
//        homeButton.setBackground(purple);
//        homeButton.setFocusPainted(false);
//        homeButton.setBorder(new EmptyBorder(12, 25, 12, 25));
//        homeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        homeButton.addActionListener(e -> {
//            dispose();
//            new Main().showHome();
//        });
//
//        panel.add(heading);
//        panel.add(Box.createVerticalStrut(15));
//        panel.add(name);
//        panel.add(Box.createVerticalStrut(20));
//        panel.add(scoreLabel);
//        panel.add(Box.createVerticalStrut(5));
//        panel.add(percentageLabel);
//        panel.add(Box.createVerticalStrut(15));
//        panel.add(feedback);
//        panel.add(Box.createVerticalStrut(30));
//        panel.add(homeButton);
//
//        add(panel);
//    }
//}


package com.quiz.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ResultFrame extends JFrame {

    private final Color NAVY = new Color(25, 32, 67);
    private final Color PURPLE = new Color(105, 76, 220);
    private final Color BG = new Color(246, 247, 252);

    public ResultFrame(
            String studentName,
            int score,
            int total,
            List<Question> questions,
            int[] selectedAnswers) {

        setTitle("Quiz Results");
        setSize(800, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Result heading
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);

        JLabel title = new JLabel("Quiz Completed!");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(NAVY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Well done, " + studentName + "!");
        welcome.setFont(new Font("SansSerif", Font.PLAIN, 18));
        welcome.setForeground(new Color(90, 95, 120));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel(score + " / " + total);
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 45));
        scoreLabel.setForeground(PURPLE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        double percentage = total == 0
                ? 0
                : score * 100.0 / total;

        JLabel percentageLabel = new JLabel(
                String.format("Your score: %.1f%%", percentage));
        percentageLabel.setFont(
                new Font("SansSerif", Font.BOLD, 18));
        percentageLabel.setForeground(NAVY);
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(8));
        header.add(welcome);
        header.add(Box.createVerticalStrut(10));
        header.add(scoreLabel);
        header.add(Box.createVerticalStrut(5));
        header.add(percentageLabel);
        header.add(Box.createVerticalStrut(15));

        mainPanel.add(header, BorderLayout.NORTH);

        // Answer review section
        JPanel reviewPanel = new JPanel();
        reviewPanel.setLayout(
                new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
        reviewPanel.setBackground(Color.WHITE);
        reviewPanel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel reviewTitle = new JLabel("Review Your Answers");
        reviewTitle.setFont(
                new Font("SansSerif", Font.BOLD, 20));
        reviewTitle.setForeground(NAVY);
        reviewTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        reviewPanel.add(reviewTitle);
        reviewPanel.add(Box.createVerticalStrut(15));

        int wrongCount = 0;

        for (int i = 0; i < questions.size(); i++) {

            Question q = questions.get(i);
            int selected = selectedAnswers[i];
            int correct = q.getCorrectAnswer();
            String[] options = q.getOptions();

            if (selected == correct) {
                continue;
            }

            wrongCount++;

            JPanel item = new JPanel();
            item.setLayout(
                    new BoxLayout(item, BoxLayout.Y_AXIS));
            item.setBackground(new Color(255, 245, 245));
            item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            new Color(240, 205, 205)),
                    new EmptyBorder(12, 14, 12, 14)));

            item.setAlignmentX(Component.LEFT_ALIGNMENT);
            item.setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, 180));

            JLabel questionLabel = new JLabel(
                    "<html><b>Question " + (i + 1) + ": "
                    + q.getQuestionText()
                    + "</b></html>");

            questionLabel.setFont(
                    new Font("SansSerif", Font.BOLD, 14));
            questionLabel.setForeground(NAVY);

            JLabel yourAnswer = new JLabel(
                    "<html>Your answer: "
                    + options[selected]
                    + "</html>");

            yourAnswer.setFont(
                    new Font("SansSerif", Font.PLAIN, 14));
            yourAnswer.setForeground(new Color(190, 45, 45));

            JLabel correctAnswer = new JLabel(
                    "<html>Correct answer: "
                    + options[correct]
                    + "</html>");

            correctAnswer.setFont(
                    new Font("SansSerif", Font.BOLD, 14));
            correctAnswer.setForeground(
                    new Color(25, 135, 75));

            item.add(questionLabel);
            item.add(Box.createVerticalStrut(8));
            item.add(yourAnswer);
            item.add(Box.createVerticalStrut(5));
            item.add(correctAnswer);

            reviewPanel.add(item);
            reviewPanel.add(Box.createVerticalStrut(12));
        }

        if (wrongCount == 0) {
            JLabel allCorrect = new JLabel(
                    "Excellent! You answered every question correctly.");
            allCorrect.setFont(
                    new Font("SansSerif", Font.BOLD, 15));
            allCorrect.setForeground(new Color(25, 135, 75));
            reviewPanel.add(allCorrect);
        }

        JScrollPane scrollPane = new JScrollPane(reviewPanel);
        scrollPane.setBorder(
                BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Back to Home button
        JButton homeButton = new JButton("Back to Home");
        homeButton.setFont(
                new Font("SansSerif", Font.BOLD, 14));
        homeButton.setForeground(Color.WHITE);
        homeButton.setBackground(PURPLE);
        homeButton.setFocusPainted(false);
        homeButton.setBorder(
                new EmptyBorder(12, 25, 12, 25));

        homeButton.addActionListener(e -> {
            dispose();
            new Main().showHome();
        });

        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.add(homeButton);

        mainPanel.add(footer, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}
