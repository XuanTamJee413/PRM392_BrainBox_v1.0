package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "quizzes",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "creatorId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Quiz {
    @PrimaryKey(autoGenerate = true)
    public int quizId;

    public String quizName;

    public String description;

    public int creatorId;

    public boolean isPublic = true;

    public long createdAt = System.currentTimeMillis();
}

