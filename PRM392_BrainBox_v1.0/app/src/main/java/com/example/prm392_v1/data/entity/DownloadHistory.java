package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "download_history",
        primaryKeys = {"userId", "docId"}, // composite key để tránh trùng
        foreignKeys = {
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Document.class,
                        parentColumns = "docId",
                        childColumns = "docId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class DownloadHistory {
    public int userId;
    public int docId;
    public long downloadedAt = System.currentTimeMillis();
}

