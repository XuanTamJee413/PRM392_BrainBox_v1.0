package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392_v1.data.entity.Quiz;

import java.util.List;

@Dao
public interface QuizDao {
    @Insert
    void insert(Quiz quiz);

    @Update
    void update(Quiz quiz);

    @Delete
    void delete(Quiz quiz);

    @Query("SELECT * FROM quizzes WHERE quizId = :id")
    Quiz getById(int id);

    @Query("SELECT * FROM quizzes WHERE creatorId = :creatorId")
    List<Quiz> getByCreator(int creatorId);

    @Query("SELECT * FROM quizzes")
    List<Quiz> getAll();
}

