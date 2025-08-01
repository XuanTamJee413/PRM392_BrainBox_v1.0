package com.example.prm392_v1.data.dao;

import androidx.room.*;

import com.example.prm392_v1.data.entity.DownloadHistory;

import java.util.List;

@Dao
public interface DownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DownloadHistory history);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DownloadHistory> historyList);

    @Query("SELECT * FROM download_history WHERE userId = :userId")
    List<DownloadHistory> getByUser(int userId);

    @Query("SELECT * FROM download_history")
    List<DownloadHistory> getAll();

    @Delete
    void delete(DownloadHistory history);

    @Query("DELETE FROM download_history")
    void clearAll();
}

