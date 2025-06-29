package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "share_log")
public class ShareLog {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int userId;
    public int docId;
    public String platform;
    public long timestamp;
}