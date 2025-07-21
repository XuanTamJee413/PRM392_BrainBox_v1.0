package com.example.prm392_v1.data.entity;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "bookmarks",
        primaryKeys = {"userId", "quizId"},
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Quiz.class,
                        parentColumns = "quizId",
                        childColumns = "quizId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Flashcard.class,
                        parentColumns = "cardId",
                        childColumns = "lastCardId",
                        onDelete = ForeignKey.SET_NULL)
        }
)
public class Bookmark {
    public int userId;
    public int quizId;
    @Nullable
    public Integer lastCardId;
    public long bookmarkedAt = System.currentTimeMillis();
}
