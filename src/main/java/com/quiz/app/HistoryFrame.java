package com.quiz.app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HistoryFrame extends JFrame {

    public HistoryFrame() {
        setTitle("QuizNova | Previous Results");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(246, 247, 252));

        JLabel heading = new JLabel("Previous Quiz Results");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        panel.add(heading, BorderLayout.NORTH);

        String[] columns = {
            "Student Name", "Score", "Total Questions",
            "Percentage", "Date"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<String[]> results = DatabaseManager.getResults();

        for (String[] result : results) {
            model.addRow(result);
        }

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        if (results.isEmpty()) {
            JLabel message = new JLabel(
                "No previous results found. Complete a quiz first.",
                SwingConstants.CENTER);
            message.setFont(new Font("SansSerif", Font.PLAIN, 14));
            panel.add(message, BorderLayout.SOUTH);
        }

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        bottom.add(closeButton);

        // Show the close button at the bottom.
        if (!results.isEmpty()) {
            panel.add(bottom, BorderLayout.SOUTH);
        }

        setContentPane(panel);
    }
}
