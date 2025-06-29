package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.Document;

import java.util.List;

@Dao
public interface DocumentDao {
    @Insert
    void insert(Document document);
    @Query("SELECT * FROM documents WHERE docId = :id")
    Document getById(int id);
    @Query("SELECT * FROM documents WHERE title LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    List<Document> search(String query);
    @Query("SELECT * FROM documents WHERE authorId = :userId")
    List<Document> getByAuthor(int userId);
}
