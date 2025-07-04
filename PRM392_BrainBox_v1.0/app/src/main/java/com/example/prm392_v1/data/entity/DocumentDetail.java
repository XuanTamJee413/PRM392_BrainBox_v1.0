package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "document_details",
        foreignKeys = @ForeignKey(
                entity = Document.class,
                parentColumns = "docId",
                childColumns = "docId",
                onDelete = ForeignKey.CASCADE
        )
)
public class DocumentDetail {
    @PrimaryKey(autoGenerate = true)
    public int docDetailId;

    public int docId;

    public String imageUrl;

    public String caption; // tùy chọn

    public long createdAt = System.currentTimeMillis();
}

