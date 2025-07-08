package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(
        tableName = "download_history",
        primaryKeys = {"userId", "targetId", "targetType"}
)
public class DownloadHistory {
    public int userId;

    public int targetId;         // dung thay cho docId hoac quizId
    @NonNull
    public String targetType;    // "document", "quiz", ...
    public long downloadedAt = System.currentTimeMillis();
}


