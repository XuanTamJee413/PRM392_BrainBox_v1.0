package com.example.prm392_v1.data.model;

import java.util.List;

public class QuizUpdateDto {
    public String quizName;
    public String description;
    public boolean isPublic;
    public List<FlashcardUpdateDto> flashcards;
    public List<Integer> flashcardIdsToDelete;
}