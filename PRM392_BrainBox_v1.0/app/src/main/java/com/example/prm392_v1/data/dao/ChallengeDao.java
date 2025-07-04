package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.Challenge;

import java.util.List;

@Dao
public interface ChallengeDao {
    @Insert
    void insert(Challenge challenge);

    @Query("SELECT * FROM challenges WHERE opponentId = :userId OR challengerId = :userId")
    List<Challenge> getByUser(int userId);
}

