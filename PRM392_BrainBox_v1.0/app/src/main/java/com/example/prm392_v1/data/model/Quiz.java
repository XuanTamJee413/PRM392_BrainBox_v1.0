package com.example.prm392_v1.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Quiz {
    @SerializedName("QuizId")
    public int quizId;

    @SerializedName("QuizName")
    public String quizName;

    @SerializedName("Description")
    public String description;

    @SerializedName("CreatorId")
    public int creatorId;

    @SerializedName("IsPublic")
    public boolean isPublic;

    @SerializedName("CreatedAt")
    public long createdAt;


    @SerializedName("Flashcards")
    public List<Flashcard> flashcards;

    public float averageRating = 0.0f;
    public int totalRatings = 0;

}