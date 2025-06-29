package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.Flashcard;

import java.util.List;

@Dao
public interface FlashcardDao {
    @Insert
    void insert(Flashcard card);
    @Query("SELECT * FROM flashcards WHERE creatorId = :userId")
    List<Flashcard> getByUser(int userId);
}
