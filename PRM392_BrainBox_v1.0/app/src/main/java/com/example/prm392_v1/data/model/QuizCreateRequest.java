package com.example.prm392_v1.data.model;

import java.util.List;

public class QuizCreateRequest {
    public String quizName;
    public String description;
    public boolean isPublic;
    public List<Flashcard> flashcards;

    public QuizCreateRequest(String quizName, String description, boolean isPublic, List<Flashcard> flashcards) {
        this.quizName = quizName;
        this.description = description;
        this.isPublic = isPublic;
        this.flashcards = flashcards;
    }
}