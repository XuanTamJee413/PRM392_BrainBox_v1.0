package com.example.prm392_v1.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tags")
public class Tag {
    @PrimaryKey(autoGenerate = true)
    public int tagId;

    @ColumnInfo(index = true)
    public String name;

    public String description;
}

