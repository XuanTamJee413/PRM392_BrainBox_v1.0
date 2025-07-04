package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_v1.data.entity.Flashcard;

import java.util.List;

@Dao
public interface FlashcardDao {
    @Insert
    void insert(Flashcard card);

    @Update
    void update(Flashcard card);

    @Delete
    void delete(Flashcard card);

    @Query("SELECT * FROM flashcards WHERE quizId = :quizId")
    List<Flashcard> getByQuizId(int quizId);

    @Query("SELECT * FROM flashcards WHERE cardId = :cardId")
    Flashcard getById(int cardId);
}

