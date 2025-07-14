package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "challenges",
        foreignKeys = {
                @ForeignKey(entity = Quiz.class,
                        parentColumns = "quizId",
                        childColumns = "quizId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "challengerId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "opponentId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Challenge {
    @PrimaryKey(autoGenerate = true)
    public int challengeId;

    public int quizId;
    public int challengerId;
    public int opponentId;
    public int status = 0;
    public int challengerScore = 0;
    public int opponentScore = 0;
    public long createdAt = System.currentTimeMillis();
}

