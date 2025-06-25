package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int commentId;

    public int docId;
    public int userId;
    public String content;
    public Integer rating;
    public long timestamp;
}
