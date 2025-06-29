package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "flashcards")
public class Flashcard {
    @PrimaryKey(autoGenerate = true)
    public int cardId;

    public String question;
    public String answer;
    public int creatorId;
}
