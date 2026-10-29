
package com.quiz.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {

    static final Color NAVY = new Color(25, 32, 67);
    static final Color PURPLE = new Color(105, 76, 220);
    static final Color BG = new Color(246, 247, 252);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().showHome());
    }

    public void showHome() {
        JFrame frame = new JFrame("QuizNova | Quiz Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 560);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(20, 20));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel logo = new JLabel("✦  QUIZNOVA");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(NAVY);
        root.add(logo, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
        content.setOpaque(false);

        JPanel hero = new JPanel();
        hero.setLayout(new BoxLayout(hero, BoxLayout.Y_AXIS));
        hero.setBackground(NAVY);
        hero.setBorder(new EmptyBorder(35, 25, 25, 25));

        JLabel tag = new JLabel("LEARN  •  PRACTICE  •  GROW");
        tag.setForeground(new Color(195, 180, 255));
        tag.setFont(new Font("SansSerif", Font.BOLD, 11));

        JLabel title = new JLabel(
            "<html>Challenge<br>yourself.</html>");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 36));

        JLabel description = new JLabel(
            "<html><div style='width:230px'>"
            + "Test your knowledge with multiple-choice "
            + "questions and get your score instantly."
            + "</div></html>");
        description.setForeground(new Color(220, 223, 239));
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));

        hero.add(tag);
        hero.add(Box.createVerticalStrut(35));
        hero.add(title);
        hero.add(Box.createVerticalStrut(15));
        hero.add(description);
        hero.add(Box.createVerticalGlue());

        JLabel stats = new JLabel("MULTIPLE CHOICE  |  INSTANT SCORE");
        stats.setForeground(new Color(195, 180, 255));
        stats.setFont(new Font("SansSerif", Font.BOLD, 10));
        hero.add(stats);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(35, 25, 25, 25));

        JLabel heading = new JLabel("Ready to begin?");
        heading.setFont(new Font("SansSerif", Font.BOLD, 25));
        heading.setForeground(NAVY);

        JLabel subtitle = new JLabel("Enter your details below.");
        subtitle.setForeground(Color.GRAY);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel nameLabel = new JLabel("STUDENT NAME");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        nameLabel.setForeground(NAVY);

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 15));
        nameField.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 42));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(220, 222, 235)),
            new EmptyBorder(8, 10, 8, 10)));

        JButton start = new JButton("Start Quiz  →");
        start.setFont(new Font("SansSerif", Font.BOLD, 14));
        start.setForeground(Color.WHITE);
        start.setBackground(PURPLE);
        start.setFocusPainted(false);
        start.setBorder(new EmptyBorder(14, 12, 14, 12));
        start.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 48));
        start.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton results = new JButton("View Previous Results");
        results.setFont(new Font("SansSerif", Font.BOLD, 13));
        results.setForeground(PURPLE);
        results.setBackground(new Color(241, 237, 255));
        results.setFocusPainted(false);
        results.setBorder(new EmptyBorder(12, 10, 12, 10));
        results.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 45));
        results.setAlignmentX(Component.LEFT_ALIGNMENT);


        start.addActionListener(e -> {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Please enter your name."
                );
                return;
            }

            new QuizFrame(name).setVisible(true);
            frame.dispose();
        });

        results.addActionListener(e ->
        new HistoryFrame().setVisible(true));

        nameField.addActionListener(e -> start.doClick());

        card.add(heading);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(nameField);
        card.add(Box.createVerticalStrut(20));
        card.add(start);
        card.add(Box.createVerticalStrut(12));
        card.add(results);

        content.add(hero);
        content.add(card);

        root.add(content, BorderLayout.CENTER);

        JLabel footer = new JLabel(
            "JAVA SWING  •  QUIZ MANAGEMENT SYSTEM");
        footer.setForeground(Color.GRAY);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));
        root.add(footer, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
    }
}
