package com.example.prm392_v1.data.dao;

import androidx.room.*;

import com.example.prm392_v1.data.entity.DownloadHistory;

import java.util.List;

@Dao
public interface DownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DownloadHistory history);

    // Thêm danh sách
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DownloadHistory> historyList);

    // Lấy theo user
    @Query("SELECT * FROM download_history WHERE userId = :userId")
    List<DownloadHistory> getByUser(int userId);

    // Lấy toàn bộ
    @Query("SELECT * FROM download_history")
    List<DownloadHistory> getAll();

    // Xoá một bản ghi
    @Delete
    void delete(DownloadHistory history);

    // Xoá toàn bộ
    @Query("DELETE FROM download_history")
    void clearAll();
}

