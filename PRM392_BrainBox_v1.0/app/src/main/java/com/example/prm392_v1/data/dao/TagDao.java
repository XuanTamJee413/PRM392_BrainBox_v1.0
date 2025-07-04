package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.DocumentTagCrossRef;
import com.example.prm392_v1.data.entity.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insert(Tag tag);

    @Query("SELECT * FROM tags")
    List<Tag> getAll();

    @Query("SELECT * FROM tags WHERE name LIKE '%' || :search || '%'")
    List<Tag> searchByName(String search);
}

