package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "comments",
        foreignKeys = {
                @ForeignKey(entity = DocumentDetail.class,
                        parentColumns = "docDetailId",
                        childColumns = "docDetailId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "id",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int commentId;

    public int docDetailId;

    public int userId;

    public String content;

    public long createdAt = System.currentTimeMillis();
}

