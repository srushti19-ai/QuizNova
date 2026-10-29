//package com.quiz.app;
//
//import javax.swing.*;
//import javax.swing.border.EmptyBorder;
//import java.awt.*;
//import java.sql.*;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//public class QuizManagementSystem extends JFrame {
//    private static final String DB_URL = "jdbc:sqlite:quiz_management.db";
//    private final Color navy = new Color(25, 39, 72);
//    private final Color blue = new Color(54, 101, 220);
//    private final Color pale = new Color(244, 247, 252);
//    private final CardLayout cards = new CardLayout();
//    private final JPanel root = new JPanel(cards);
//    private JTextField nameField;
//    private JLabel questionLabel, progressLabel;
//    private JPanel optionsPanel;
//    private ButtonGroup optionGroup;
//    private JButton nextButton;
//    private final List<Question> questions = new ArrayList<>();
//    private int current = 0, score = 0;
//    private String studentName = "";
//    private final int[] answers = new int[100];
//
//    public QuizManagementSystem() {
//        setTitle("Quiz Management System");
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setSize(900, 620);
//        setMinimumSize(new Dimension(760, 540));
//        setLocationRelativeTo(null);
//        initDatabase();
//        root.add(homePanel(), "home");
//        root.add(quizPanel(), "quiz");
//        root.add(resultsPanel(), "results");
//        setContentPane(root);
//        cards.show(root, "home");
//    }
//
//    private void initDatabase() {
//        try (Connection c = DriverManager.getConnection(DB_URL);
//             Statement s = c.createStatement()) {
//            s.executeUpdate("CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT NOT NULL, a TEXT NOT NULL, b TEXT NOT NULL, c TEXT NOT NULL, d TEXT NOT NULL, correct INTEGER NOT NULL)");
//            s.executeUpdate("CREATE TABLE IF NOT EXISTS results (id INTEGER PRIMARY KEY AUTOINCREMENT, student_name TEXT NOT NULL, score INTEGER NOT NULL, total INTEGER NOT NULL, attempted_at TEXT NOT NULL)");
//            try (ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM questions")) {
//                if (rs.next() && rs.getInt(1) == 0) seedQuestions(c);
//            }
//        } catch (SQLException ex) {
//            showError("Database setup failed: " + ex.getMessage());
//        }
//    }
//
//    private void seedQuestions(Connection c) throws SQLException {
//        String[][] data = {
//            {"Which language runs on the Java Virtual Machine?", "C", "Java", "HTML", "SQL", "1"},
//            {"Which keyword creates an object in Java?", "make", "class", "new", "this", "2"},
//            {"Which method is the usual Java program entry point?", "start()", "main()", "run()", "init()", "1"},
//            {"Which collection stores unique elements?", "ArrayList", "LinkedList", "HashSet", "Vector", "2"},
//            {"Which SQL command retrieves records?", "GET", "OPEN", "SELECT", "FETCHALL", "2"},
//            {"Which component is used for a button in Swing?", "JButton", "JFrame", "JLabel", "JPanel", "0"},
//            {"What does JDBC connect Java to?", "Databases", "Images", "CSS", "Operating systems only", "0"},
//            {"Which type stores true or false?", "int", "String", "boolean", "char", "2"},
//            {"Which SQL clause filters rows?", "ORDER BY", "WHERE", "GROUP BY", "JOIN", "1"},
//            {"Which keyword is used to inherit a class in Java?", "implements", "extends", "inherits", "superclass", "1"}
//        };
//        try (PreparedStatement p = c.prepareStatement("INSERT INTO questions(question,a,b,c,d,correct) VALUES(?,?,?,?,?,?)")) {
//            for (String[] row : data) {
//                for (int i = 0; i < 5; i++) p.setString(i + 1, row[i]);
//                p.setInt(6, Integer.parseInt(row[5]));
//                p.addBatch();
//            }
//            p.executeBatch();
//        }
//    }
//
//    private JPanel homePanel() {
//        JPanel page = pagePanel();
//        page.add(header("QUIZ MANAGEMENT SYSTEM", "A simple, smart way to assess learning"));
//        JPanel center = new JPanel(new GridBagLayout());
//        center.setOpaque(false);
//        JPanel box = new JPanel();
//        box.setBackground(Color.WHITE);
//        box.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(224, 230, 240)),
//                new EmptyBorder(28, 34, 28, 34)));
//        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
//        JLabel welcome = new JLabel("Welcome to the Quiz");
//        welcome.setFont(new Font("SansSerif", Font.BOLD, 26));
//        welcome.setForeground(navy);
//        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
//        JLabel desc = new JLabel("<html>Answer multiple-choice questions and receive your score instantly.<br>Results are saved locally for future reference.</html>");
//        desc.setFont(new Font("SansSerif", Font.PLAIN, 14));
//        desc.setForeground(new Color(90, 101, 120));
//        desc.setBorder(new EmptyBorder(10, 0, 22, 0));
//        desc.setAlignmentX(Component.LEFT_ALIGNMENT);
//        JLabel nameLabel = new JLabel("Student name");
//        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
//        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        nameField = new JTextField();
//        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
//        nameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
//        nameField.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createLineBorder(new Color(210, 218, 232)), new EmptyBorder(8, 10, 8, 10)));
//        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
//        JButton start = primaryButton("Start Quiz");
//        start.setAlignmentX(Component.LEFT_ALIGNMENT);
//        start.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
//        start.addActionListener(e -> startQuiz());
//        JButton history = secondaryButton("View Saved Results");
//        history.setAlignmentX(Component.LEFT_ALIGNMENT);
//        history.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
//        history.addActionListener(e -> showResults());
//        box.add(welcome); box.add(desc); box.add(nameLabel); box.add(Box.createVerticalStrut(8));
//        box.add(nameField); box.add(Box.createVerticalStrut(18)); box.add(start);
//        box.add(Box.createVerticalStrut(10)); box.add(history);
//        box.setPreferredSize(new Dimension(480, 350));
//        center.add(box);
//        page.add(center, BorderLayout.CENTER);
//        page.add(footer(), BorderLayout.SOUTH);
//        return page;
//    }
//
//    private JPanel quizPanel() {
//        JPanel page = pagePanel();
//        page.add(header("KNOWLEDGE CHECK", "Choose one answer for each question"));
//        JPanel content = new JPanel(new BorderLayout(0, 18));
//        content.setOpaque(false);
//        content.setBorder(new EmptyBorder(26, 45, 25, 45));
//        JPanel top = new JPanel(new BorderLayout());
//        top.setOpaque(false);
//        progressLabel = new JLabel("Question 1 of 10");
//        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
//        progressLabel.setForeground(blue);
//        top.add(progressLabel, BorderLayout.WEST);
//        JLabel tag = new JLabel("MULTIPLE CHOICE");
//        tag.setForeground(new Color(95, 105, 125));
//        top.add(tag, BorderLayout.EAST);
//        content.add(top, BorderLayout.NORTH);
//        JPanel card = new JPanel();
//        card.setBackground(Color.WHITE);
//        card.setBorder(new EmptyBorder(24, 26, 24, 26));
//        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
//        questionLabel = new JLabel("Question appears here");
//        questionLabel.setFont(new Font("SansSerif", Font.BOLD, 21));
//        questionLabel.setForeground(navy);
//        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        optionsPanel = new JPanel();
//        optionsPanel.setOpaque(false);
//        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
//        optionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
//        card.add(questionLabel);
//        card.add(Box.createVerticalStrut(25));
//        card.add(optionsPanel);
//        content.add(card, BorderLayout.CENTER);
//        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        buttons.setOpaque(false);
//        JButton home = secondaryButton("Cancel");
//        home.addActionListener(e -> cards.show(root, "home"));
//        nextButton = primaryButton("Next Question");
//        nextButton.addActionListener(e -> nextQuestion());
//        buttons.add(home); buttons.add(nextButton);
//        content.add(buttons, BorderLayout.SOUTH);
//        page.add(content, BorderLayout.CENTER);
//        page.add(footer(), BorderLayout.SOUTH);
//        return page;
//    }
//
//    private void startQuiz() {
//        studentName = nameField.getText().trim();
//        if (studentName.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Please enter your name to start.", "Name required", JOptionPane.WARNING_MESSAGE);
//            nameField.requestFocus();
//            return;
//        }
//        questions.clear();
//        try (Connection c = DriverManager.getConnection(DB_URL);
//             Statement s = c.createStatement();
//             ResultSet rs = s.executeQuery("SELECT id,question,a,b,c,d,correct FROM questions ORDER BY id")) {
//            while (rs.next()) questions.add(new Question(rs.getInt("id"), rs.getString("question"),
//                    new String[]{rs.getString("a"), rs.getString("b"), rs.getString("c"), rs.getString("d")}, rs.getInt("correct")));
//        } catch (SQLException ex) {
//            showError("Could not load questions: " + ex.getMessage()); return;
//        }
//        if (questions.isEmpty()) { showError("No questions found in the database."); return; }
//        current = 0; score = 0;
//        for (int i = 0; i < answers.length; i++) answers[i] = -1;
//        showQuestion();
//        cards.show(root, "quiz");
//    }
//
//    private void showQuestion() {
//        Question q = questions.get(current);
//        progressLabel.setText("QUESTION " + (current + 1) + " OF " + questions.size());
//        questionLabel.setText("<html><div style='width:620px'>" + (current + 1) + ". " + escape(q.text) + "</div></html>");
//        optionsPanel.removeAll();
//        optionGroup = new ButtonGroup();
//        String[] letters = {"A", "B", "C", "D"};
//        for (int i = 0; i < q.options.length; i++) {
//            final int idx = i;
//            JRadioButton rb = new JRadioButton(letters[i] + "   " + q.options[i]);
//            rb.setFont(new Font("SansSerif", Font.PLAIN, 16));
//            rb.setForeground(new Color(45, 55, 75));
//            rb.setOpaque(true);
//            rb.setBackground(pale);
//            rb.setBorder(new EmptyBorder(13, 12, 13, 12));
//            rb.setAlignmentX(Component.LEFT_ALIGNMENT);
//            rb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
//            rb.addActionListener(e -> answers[current] = idx);
//            optionGroup.add(rb);
//            optionsPanel.add(rb);
//            optionsPanel.add(Box.createVerticalStrut(9));
//            if (answers[current] == i) rb.setSelected(true);
//        }
//        nextButton.setText(current == questions.size() - 1 ? "Submit Quiz" : "Next Question");
//        optionsPanel.revalidate(); optionsPanel.repaint();
//    }
//
//    private void nextQuestion() {
//        if (answers[current] == -1) {
//            JOptionPane.showMessageDialog(this, "Please select an answer before continuing.", "Answer required", JOptionPane.INFORMATION_MESSAGE);
//            return;
//        }
//        if (current < questions.size() - 1) {
//            current++;
//            showQuestion();
//        } else {
//            finishQuiz();
//        }
//    }
//
//    private void finishQuiz() {
//        score = 0;
//        for (int i = 0; i < questions.size(); i++) if (answers[i] == questions.get(i).correct) score++;
//        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        try (Connection c = DriverManager.getConnection(DB_URL);
//             PreparedStatement p = c.prepareStatement("INSERT INTO results(student_name,score,total,attempted_at) VALUES(?,?,?,?)")) {
//            p.setString(1, studentName); p.setInt(2, score); p.setInt(3, questions.size()); p.setString(4, date); p.executeUpdate();
//        } catch (SQLException ex) {
//            showError("Score calculated, but result could not be saved: " + ex.getMessage());
//        }
//        JPanel result = pagePanel();
//        result.add(header("QUIZ COMPLETED", "Your result has been calculated and saved"));
//        JPanel center = new JPanel(new GridBagLayout()); center.setOpaque(false);
//        JPanel box = new JPanel(); box.setBackground(Color.WHITE); box.setBorder(new EmptyBorder(35, 55, 35, 55));
//        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
//        JLabel done = new JLabel("Well done, " + studentName + "!");
//        done.setFont(new Font("SansSerif", Font.BOLD, 25)); done.setForeground(navy); done.setAlignmentX(Component.CENTER_ALIGNMENT);
//        JLabel marks = new JLabel(score + " / " + questions.size());
//        marks.setFont(new Font("SansSerif", Font.BOLD, 54)); marks.setForeground(blue); marks.setAlignmentX(Component.CENTER_ALIGNMENT);
//        JLabel pct = new JLabel("Percentage: " + Math.round(score * 100.0 / questions.size()) + "%");
//        pct.setFont(new Font("SansSerif", Font.PLAIN, 18)); pct.setForeground(new Color(80, 90, 110)); pct.setAlignmentX(Component.CENTER_ALIGNMENT);
//        JLabel msg = new JLabel(score >= questions.size() * 0.6 ? "Great effort! Keep learning." : "Keep practising—you can improve!");
//        msg.setFont(new Font("SansSerif", Font.PLAIN, 15)); msg.setAlignmentX(Component.CENTER_ALIGNMENT);
//        JButton home = primaryButton("Back to Home"); home.setAlignmentX(Component.CENTER_ALIGNMENT);
//        home.addActionListener(e -> { root.remove(result); root.add(homePanel(), "home"); cards.show(root, "home"); });
//        JButton history = secondaryButton("View Results History"); history.setAlignmentX(Component.CENTER_ALIGNMENT);
//        history.addActionListener(e -> showResults());
//        box.add(done); box.add(Box.createVerticalStrut(15)); box.add(marks); box.add(pct); box.add(Box.createVerticalStrut(12)); box.add(msg);
//        box.add(Box.createVerticalStrut(25)); box.add(home); box.add(Box.createVerticalStrut(10)); box.add(history);
//        center.add(box); result.add(center, BorderLayout.CENTER); result.add(footer(), BorderLayout.SOUTH);
//        root.add(result, "finished"); cards.show(root, "finished");
//    }
//
//    private JPanel resultsPanel() {
//        JPanel page = pagePanel();
//        page.add(header("RESULT MANAGEMENT", "Saved student scores from the SQLite database"));
//        JPanel middle = new JPanel(new BorderLayout(0, 15)); middle.setOpaque(false);
//        middle.setBorder(new EmptyBorder(20, 35, 20, 35));
//        String[] cols = {"ID", "Student Name", "Score", "Total", "Percentage", "Attempted At"};
//        JTable table = new JTable(new Object[0][6], cols);
//        table.setRowHeight(28); table.setFont(new Font("SansSerif", Font.PLAIN, 13));
//        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
//        JScrollPane scroll = new JScrollPane(table);
//        middle.add(scroll, BorderLayout.CENTER);
//        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bottom.setOpaque(false);
//        JButton back = secondaryButton("Back to Home");
//        back.addActionListener(e -> cards.show(root, "home"));
//        JButton refresh = primaryButton("Refresh Results");
//        refresh.addActionListener(e -> loadResults(table));
//        bottom.add(back); bottom.add(refresh); middle.add(bottom, BorderLayout.SOUTH);
//        page.add(middle, BorderLayout.CENTER); page.add(footer(), BorderLayout.SOUTH);
//        loadResults(table);
//        return page;
//    }
//
//    private void loadResults(JTable table) {
//        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
//                new String[]{"ID", "Student Name", "Score", "Total", "Percentage", "Attempted At"}, 0) {
//            public boolean isCellEditable(int row, int column) { return false; }
//        };
//        try (Connection c = DriverManager.getConnection(DB_URL);
//             Statement s = c.createStatement();
//             ResultSet rs = s.executeQuery("SELECT * FROM results ORDER BY id DESC")) {
//            while (rs.next()) {
//                int sc = rs.getInt("score"), total = rs.getInt("total");
//                model.addRow(new Object[]{rs.getInt("id"), rs.getString("student_name"), sc, total,
//                        Math.round(sc * 100.0 / total) + "%", rs.getString("attempted_at")});
//            }
//        } catch (SQLException ex) { showError("Could not load results: " + ex.getMessage()); }
//        table.setModel(model);
//    }
//
//    private void showResults() {
//        // Rebuild the results panel so the latest saved records are shown.
//        root.remove(2);
//        root.add(resultsPanel(), "results");
//        cards.show(root, "results");
//    }
//
//    private JPanel pagePanel() {
//        JPanel p = new JPanel(new BorderLayout());
//        p.setBackground(pale);
//        return p;
//    }
//
//    private JPanel header(String title, String subtitle) {
//        JPanel h = new JPanel();
//        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
//        h.setBackground(navy);
//        h.setBorder(new EmptyBorder(23, 35, 23, 35));
//        JLabel t = new JLabel(title); t.setForeground(Color.WHITE); t.setFont(new Font("SansSerif", Font.BOLD, 21));
//        JLabel s = new JLabel(subtitle); s.setForeground(new Color(205, 215, 238)); s.setFont(new Font("SansSerif", Font.PLAIN, 13));
//        s.setBorder(new EmptyBorder(6, 0, 0, 0));
//        t.setAlignmentX(Component.LEFT_ALIGNMENT); s.setAlignmentX(Component.LEFT_ALIGNMENT);
//        h.add(t); h.add(s);
//        return h;
//    }
//
//    private JPanel footer() {
//        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER));
//        f.setBackground(pale);
//        JLabel label = new JLabel("QUIZ MANAGEMENT SYSTEM   •   JAVA SWING + JDBC + SQLITE");
//        label.setForeground(new Color(130, 140, 158)); label.setFont(new Font("SansSerif", Font.PLAIN, 10));
//        f.add(label); return f;
//    }
//
//    private JButton primaryButton(String text) {
//        JButton b = new JButton(text); b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        b.setBackground(blue); b.setForeground(Color.WHITE); b.setFont(new Font("SansSerif", Font.BOLD, 14));
//        b.setBorder(new EmptyBorder(11, 20, 11, 20)); return b;
//    }
//
//    private JButton secondaryButton(String text) {
//        JButton b = new JButton(text); b.setFocusPainted(false); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//        b.setBackground(Color.WHITE); b.setForeground(navy); b.setFont(new Font("SansSerif", Font.BOLD, 13));
//        b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210, 218, 232)), new EmptyBorder(9, 15, 9, 15)));
//        return b;
//    }
//
//    private String escape(String s) {
//        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
//    }
//
//    private void showError(String message) {
//        JOptionPane.showMessageDialog(this, message, "Quiz Management System", JOptionPane.ERROR_MESSAGE);
//    }
//
//    private static class Question {
//        int id, correct; String text; String[] options;
//        Question(int id, String text, String[] options, int correct) {
//            this.id = id; this.text = text; this.options = options; this.correct = correct;
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new QuizManagementSystem().setVisible(true));
//    }
//}
