package com.example.prm392_v1.data;

import android.content.Context;

import androidx.room.RoomDatabase.Callback;
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
                Quiz.class,
                Flashcard.class,
                Bookmark.class,
                Challenge.class,
                Comment.class,
                Document.class,
                DocumentDetail.class,
                DocumentTagCrossRef.class,
                DownloadHistory.class,
                Notification.class,
                RatingQuiz.class,
                Tag.class
        },
        version = 1,
        exportSchema = false
)
public abstract class BrainBoxDatabase extends RoomDatabase {

    private static volatile BrainBoxDatabase INSTANCE;

    // ==== DAO declarations ====
    public abstract UserDao userDao();
    public abstract DocumentDao documentDao();
    public abstract CommentDao commentDao();
    public abstract FlashcardDao flashcardDao();
    public abstract TagDao tagDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract QuizDao quizDao();
    public abstract RatingDao ratingDao();
    public abstract DownloadDao downloadDao();
    public abstract DocumentDetailDao documentDetailDao();
    public abstract NotificationDao notificationDao();
    public abstract ChallengeDao challengeDao();

    // ==== Singleton builder ====
    public static BrainBoxDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BrainBoxDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    BrainBoxDatabase.class,
                                    "brainbox-db"
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(new RoomDatabase.Callback() {
                                public void onConfigure(@NonNull SupportSQLiteDatabase db) {
                                    db.setForeignKeyConstraintsEnabled(true);
                                }

                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    //seedData(INSTANCE);
                                }
                            })

                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ==== Seed default users ====
    private static void seedData(BrainBoxDatabase db) {
        Executors.newSingleThreadExecutor().execute(() -> {
            UserDao userDao = db.userDao();
            long now = System.currentTimeMillis();
            long nextMonth = now + 30L * 24 * 60 * 60 * 1000;

            try {
                userDao.insert(new User(0, "admin", hash("123456"), "admin", "admin@brainbox.com", true, "https://i.pravatar.cc/150?img=1", now, nextMonth));
                userDao.insert(new User(0, "teacher.jane", hash("123456"), "teacher", "jane@brainbox.com", true, "https://i.pravatar.cc/150?img=12", now, 0));
                userDao.insert(new User(0, "student.bob", hash("123456"), "user", "bob@student.edu", true, "https://i.pravatar.cc/150?img=25", now, nextMonth));
                userDao.insert(new User(0, "student.alice", hash("123456"), "user", "alice@student.edu", false, "https://i.pravatar.cc/150?img=30", now, 0));
            } catch (Exception e) {
                e.printStackTrace();
            }

            QuizDao quizDao = db.quizDao();
            try {
                Quiz quiz1 = new Quiz();
                quiz1.quizName = "PRM392 PT1";
                quiz1.description = "Các câu hỏi về mobile app";
                quiz1.creatorId = 1;

                Quiz quiz2 = new Quiz();
                quiz2.quizName = "EXE101";
                quiz2.description = "Khởi Nghiệp";
                quiz2.creatorId = 1;

                quizDao.insert(quiz1);
                quizDao.insert(quiz2);

            } catch (Exception e) {
                e.printStackTrace();
            }
            DocumentDao documentDao = db.documentDao();
            try {
                Document doc1 = new Document();
                doc1.title = "Lập trình Android cơ bản";
                doc1.content = "Tài liệu hướng dẫn lập trình Android từ cơ bản đến nâng cao.";
                doc1.authorId = 1;
                doc1.isPublic = true;
                doc1.views = 25;
                doc1.createdAt = now;

                Document doc2 = new Document();
                doc2.title = "Cơ sở dữ liệu nâng cao";
                doc2.content = "Giải thích các khái niệm nâng cao trong thiết kế database.";
                doc2.authorId = 2;
                doc2.isPublic = false;
                doc2.views = 12;
                doc2.createdAt = now;

                Document doc3 = new Document();
                doc3.title = "Mạng máy tính";
                doc3.content = "Giới thiệu về các giao thức mạng và kiến trúc OSI.";
                doc3.authorId = 3;
                doc3.isPublic = true;
                doc3.views = 40;
                doc3.createdAt = now;

                documentDao.insert(doc1);
                documentDao.insert(doc2);
                documentDao.insert(doc3);
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
    }

    // ==== SHA-256 Hashing ====
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
