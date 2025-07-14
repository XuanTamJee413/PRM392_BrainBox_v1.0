package com.example.prm392_v1.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "download_history")
public class DownloadHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int targetId;
    @NonNull
    public String targetType;
    public long downloadedAt = System.currentTimeMillis();
}


