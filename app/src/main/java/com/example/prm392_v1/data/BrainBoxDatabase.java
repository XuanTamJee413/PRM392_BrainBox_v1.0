package com.example.prm392_v1.data;

import static androidx.activity.OnBackPressedDispatcherKt.addCallback;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.prm392_v1.data.dao.*;
import com.example.prm392_v1.data.entity.*;

import java.util.concurrent.Executors;

@Database(
        entities = {
                User.class,
                Document.class,
                Comment.class,
                SavedDocument.class,
                Flashcard.class,
                Tag.class,
                DocumentTagCrossRef.class,
                DownloadHistory.class,
                ShareLog.class,
                QuizProgress.class,
                Bookmark.class
        },
        version = 1
)
public abstract class BrainBoxDatabase extends RoomDatabase {

    private static volatile BrainBoxDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract DocumentDao documentDao();
    public abstract CommentDao commentDao();
    //public abstract SavedDao savedDao();
    public abstract FlashcardDao flashcardDao();
    public abstract TagDao tagDao();
    //public abstract ShareDao shareDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract ProgressDao progressDao();

    public static BrainBoxDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BrainBoxDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BrainBoxDatabase.class,
                            "brainbox-db"
                    ).fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);

                                    seedData(INSTANCE);
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static void seedData(BrainBoxDatabase db) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserDao userDao = db.userDao();
            try {
                userDao.insert(new User("admin", hash("123456"), "admin"));
                userDao.insert(new User("teacher", hash("123456"), "teacher"));
                userDao.insert(new User("student", hash("123456"), "user"));
                userDao.insert(new User("student", hash("123456"), "user"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static String hash(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
