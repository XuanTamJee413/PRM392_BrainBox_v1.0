package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.Document;

import java.util.List;

@Dao
public interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Document doc);

    @Query("SELECT * FROM documents WHERE docId = :id LIMIT 1")
    Document getByDocId(int id);

    @Query("SELECT * FROM documents WHERE authorId = :userId")
    List<Document> getByAuthor(int userId);

    @Query("SELECT * FROM documents WHERE isPublic = 1")
    List<Document> getAllPublic();
}

