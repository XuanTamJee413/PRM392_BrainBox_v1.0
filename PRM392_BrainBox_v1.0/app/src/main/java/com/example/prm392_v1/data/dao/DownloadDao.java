package com.example.prm392_v1.data.dao;

import androidx.room.*;

import com.example.prm392_v1.data.entity.DownloadHistory;

import java.util.List;

@Dao
public interface DownloadDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DownloadHistory history);

    @Query("SELECT * FROM download_history WHERE userId = :userId")
    List<DownloadHistory> getByUser(int userId);
}

