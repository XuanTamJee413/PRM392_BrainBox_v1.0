package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
// bang trung gian cua tag va document
@Entity(tableName = "document_tag_crossref", primaryKeys = {"docId", "tagId"})
public class DocumentTagCrossRef {
    public int docId;
    public int tagId;
}
