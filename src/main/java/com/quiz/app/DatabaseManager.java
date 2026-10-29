package com.quiz.app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:quiz.db";
    
    static {
        initializeDatabase();
    }

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {

        String questionsTable =
            "CREATE TABLE IF NOT EXISTS questions (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "question_text TEXT NOT NULL, " +
            "option_a TEXT NOT NULL, " +
            "option_b TEXT NOT NULL, " +
            "option_c TEXT NOT NULL, " +
            "option_d TEXT NOT NULL, " +
            "correct_index INTEGER NOT NULL)";

        String resultsTable =
            "CREATE TABLE IF NOT EXISTS results (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "student_name TEXT NOT NULL, " +
            "score INTEGER NOT NULL, " +
            "total INTEGER NOT NULL, " +
            "percentage REAL NOT NULL, " +
            "submitted_at TEXT DEFAULT CURRENT_TIMESTAMP)";

        try (Connection con = connect();
             Statement st = con.createStatement()) {

            st.execute(questionsTable);
            st.execute(resultsTable);

            ResultSet rs = st.executeQuery(
                "SELECT COUNT(*) FROM questions");

            if (rs.next() && rs.getInt(1) == 0) {
                insertSampleQuestions(con);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(
                "Database initialization failed!", e);
        }
    }

    private static void insertSampleQuestions(Connection con)
            throws SQLException {

        String sql =
            "INSERT INTO questions " +
            "(question_text, option_a, option_b, " +
            "option_c, option_d, correct_index) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

        Object[][] data = {
            {"Which language is used in this project?",
             "Python", "Java", "C++", "PHP", 1},

            {"Which keyword creates an object in Java?",
             "make", "create", "new", "object", 2},

            {"Which component creates a button in Swing?",
             "JLabel", "JFrame", "JButton", "JPanel", 2},

            {"Which SQL command retrieves data?",
             "INSERT", "SELECT", "UPDATE", "DELETE", 1},

            {"What is SQLite?",
             "Programming language", "Web browser",
             "Database", "Operating system", 2}
        };

        try (PreparedStatement ps =
                 con.prepareStatement(sql)) {

            for (Object[] q : data) {
                ps.setString(1, (String) q[0]);
                ps.setString(2, (String) q[1]);
                ps.setString(3, (String) q[2]);
                ps.setString(4, (String) q[3]);
                ps.setString(5, (String) q[4]);
                ps.setInt(6, (Integer) q[5]);
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    public static List<Question> getQuestions() {

        List<Question> questions = new ArrayList<>();

        String sql = "SELECT * FROM questions ORDER BY id";

        try (Connection con = connect();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                String[] options = {
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d")
                };

                questions.add(new Question(
                    rs.getInt("id"),
                    rs.getString("question_text"),
                    options,
                    rs.getInt("correct_index")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Questions loaded: " + questions.size());
        return questions;
    }

    public static void saveResult(
            String name, int score, int total) {

        String sql =
            "INSERT INTO results " +
            "(student_name, score, total, percentage) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection con = connect();
             PreparedStatement ps =
                 con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, score);
            ps.setInt(3, total);

            double percentage =
                total == 0 ? 0 : score * 100.0 / total;

            ps.setDouble(4, percentage);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

public static java.util.List<String[]> getResults() {
    java.util.List<String[]> results = new java.util.ArrayList<>();

    String sql = "SELECT student_name, score, total, percentage, submitted_at "
               + "FROM results ORDER BY id DESC";

    try (Connection con = connect();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(sql)) {

        while (rs.next()) {
            String[] row = {
                rs.getString("student_name"),
                rs.getInt("score") + "",
                rs.getInt("total") + "",
                String.format("%.2f%%", rs.getDouble("percentage")),
                rs.getString("submitted_at")
            };

            results.add(row);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return results;
}

}
