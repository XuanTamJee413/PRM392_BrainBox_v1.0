package com.example.prm392_v1.data.entity;

import androidx.room.Entity;

@Entity(tableName = "quiz_progress", primaryKeys = {"userId", "cardId"})
public class QuizProgress {
    public int userId;
    public int cardId;
    public boolean isCorrect;
    public long timestamp;
}
