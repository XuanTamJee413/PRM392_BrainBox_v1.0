package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "quiz_ratings",
        foreignKeys = {
                @ForeignKey(entity = Quiz.class,
                        parentColumns = "quizId",
                        childColumns = "quizId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class RatingQuiz {
    @PrimaryKey(autoGenerate = true)
    public int ratingId;

    public int quizId;

    public int userId;

    public int rating;

    public String comment;

    public long ratedAt = System.currentTimeMillis();
}

