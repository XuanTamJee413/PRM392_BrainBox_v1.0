package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
        tableName = "document_tag_crossref",
        primaryKeys = {"docId", "tagId"},
        foreignKeys = {
                @ForeignKey(entity = Document.class,
                        parentColumns = "docId",
                        childColumns = "docId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Tag.class,
                        parentColumns = "tagId",
                        childColumns = "tagId",
                        onDelete = ForeignKey.CASCADE)
        }
)
public class DocumentTagCrossRef {
    public int docId;
    public int tagId;
}

