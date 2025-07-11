package com.example.prm392_v1.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.prm392_v1.data.dao.CommentDao;
import com.example.prm392_v1.data.dao.DocumentDao;
import com.example.prm392_v1.data.dao.DocumentDetailDao;
import com.example.prm392_v1.data.entity.Comment;
import com.example.prm392_v1.data.entity.Document;
import com.example.prm392_v1.data.entity.DocumentDetail;
import com.example.prm392_v1.data.entity.User;

@Database(entities = {Document.class, DocumentDetail.class, Comment.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CommentDao commentDao();
    public abstract DocumentDao documentDao();
    public abstract DocumentDetailDao documentDetailDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}