package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "documents")
public class Document {
    @PrimaryKey(autoGenerate = true)
    public int docId;

    public String title;

    public String content;

    public int authorId;

    public boolean isPublic = true;

    public int views = 0;

    public long createdAt = System.currentTimeMillis();
}
