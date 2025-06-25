package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Insert
    void insert(Comment comment);
    @Query("SELECT * FROM comments WHERE docId = :docId ORDER BY timestamp DESC")
    List<Comment> getByDocument(int docId);
}
