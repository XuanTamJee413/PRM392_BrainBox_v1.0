package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.QuizProgress;

import java.util.List;

@Dao
public interface ProgressDao {
    @Insert
    void insert(QuizProgress progress);
    @Query("SELECT * FROM quiz_progress WHERE userId = :userId")
    List<QuizProgress> getProgressByUser(int userId);
}
