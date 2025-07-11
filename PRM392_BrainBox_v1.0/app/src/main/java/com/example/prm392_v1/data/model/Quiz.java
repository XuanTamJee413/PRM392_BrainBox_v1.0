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

    // It's good practice to mark this as @SerializedName if your API also returns it
    // but if it's only on the client-side, it's fine without it.
    @SerializedName("Flashcards")
    public List<Flashcard> flashcards;

    // NEW: Fields to store average rating and total count (not from API directly in this flow)
    public float averageRating = 0.0f; // Calculated client-side
    public int totalRatings = 0;       // Calculated client-side
}