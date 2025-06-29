package com.example.prm392_v1.data.entity;

import androidx.room.Entity;

@Entity(tableName = "bookmarks", primaryKeys = {"userId", "docId"})
public class Bookmark {
    public int userId;
    public int docId;
    public long bookmarkedAt;
}