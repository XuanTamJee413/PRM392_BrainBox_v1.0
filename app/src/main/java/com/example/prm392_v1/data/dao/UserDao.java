package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getByUsername(String username);

    @Query("SELECT * FROM users WHERE id = :id")
    User getById(int id);
}
