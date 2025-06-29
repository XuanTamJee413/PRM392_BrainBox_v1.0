package com.example.prm392_v1.data.entity;

import androidx.room.Entity;

@Entity(tableName = "saved_documents", primaryKeys = {"userId", "docId"})
public class SavedDocument {
    public int userId;
    public int docId;
    public long savedAt;
}
