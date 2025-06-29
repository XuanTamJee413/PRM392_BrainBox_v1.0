package com.example.prm392_v1.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// giong bang cate nhung ma quan he N-N
@Entity(tableName = "tags")
public class Tag {
    @PrimaryKey(autoGenerate = true)
    public int tagId;
    public String name;
}
