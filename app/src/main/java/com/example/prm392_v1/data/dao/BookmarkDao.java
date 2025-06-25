package com.example.prm392_v1.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392_v1.data.entity.Bookmark;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    void insert(Bookmark bookmark);
    @Query("SELECT * FROM bookmarks WHERE userId = :userId")
    List<Bookmark> getBookmarks(int userId);
}
