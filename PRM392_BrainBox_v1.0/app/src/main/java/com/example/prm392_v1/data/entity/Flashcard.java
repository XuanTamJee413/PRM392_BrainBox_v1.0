package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "flashcards",
        foreignKeys = @ForeignKey(
                entity = Quiz.class,
                parentColumns = "quizId",
                childColumns = "quizId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Flashcard {
    @PrimaryKey(autoGenerate = true)
    public int cardId;

    public int quizId;

    public String question;

    public String option1;
    public String option2;
    public String option3;
    public String option4;

    public int answer; // 1, 2, 3, 4

    public int creatorId;

    public long createdAt = System.currentTimeMillis();
}

