package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "notifications",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Notification {
    @PrimaryKey(autoGenerate = true)
    public int notificationId;

    public int userId;

    public String content;

    public String type; // ví dụ: "comment", "challenge", "quiz", v.v.

    public int relatedId; // liên kết đến quizId, docId... tùy loại

    public boolean isRead = false;

    public long readAt = 0;

    public long createdAt = System.currentTimeMillis();
}

