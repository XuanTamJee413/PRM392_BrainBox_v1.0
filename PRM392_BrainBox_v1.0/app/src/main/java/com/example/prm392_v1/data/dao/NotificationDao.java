package com.example.prm392_v1.data.dao;

import androidx.room.*;

import com.example.prm392_v1.data.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert
    void insert(Notification notification);

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    List<Notification> getByUser(int userId);

    @Query("UPDATE notifications SET isRead = 1, readAt = :readAt WHERE notificationId = :id")
    void markAsRead(int id, long readAt);
}

