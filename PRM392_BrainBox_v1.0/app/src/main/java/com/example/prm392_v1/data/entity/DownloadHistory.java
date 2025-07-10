package com.example.prm392_v1.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "download_history")
public class DownloadHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int targetId;         // dung thay cho docId hoac quizId
    @NonNull
    public String targetType;    // "document", "quiz", ...
    public long downloadedAt = System.currentTimeMillis();
}


