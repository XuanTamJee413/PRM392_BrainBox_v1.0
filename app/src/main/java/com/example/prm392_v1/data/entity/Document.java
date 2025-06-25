package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "documents")
public class Document {
    @PrimaryKey(autoGenerate = true)
    public int docId;

    public String title;
    public String content;
    public int authorId;
    public long timestamp;
}
