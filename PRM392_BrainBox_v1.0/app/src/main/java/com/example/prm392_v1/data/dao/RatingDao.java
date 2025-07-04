package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.RatingQuiz;

import java.util.List;

@Dao
public interface RatingDao {
    @Insert
    void insert(RatingQuiz rating);

    @Query("SELECT * FROM quiz_ratings WHERE quizId = :quizId")
    List<RatingQuiz> getByQuizId(int quizId);

    @Query("SELECT AVG(rating) FROM quiz_ratings WHERE quizId = :quizId")
    float getAverageRating(int quizId);
}

