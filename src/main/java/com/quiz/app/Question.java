package com.quiz.app;

public class Question {

    private int id;
    private String questionText;
    private String[] options;
    private int correctAnswer;

    public Question(int id, String questionText,
                    String[] options, int correctAnswer) {
        this.id = id;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
