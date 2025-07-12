package com.example.prm392_v1.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.prm392_v1.data.BrainBoxDatabase;
import com.example.prm392_v1.data.dao.FlashcardDao;
import com.example.prm392_v1.data.dao.QuizDao;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.QuizDto;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.auth.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizDownloader {

    public static void downloadQuizWithFlashcards(Context context, com.example.prm392_v1.data.model.Quiz quizModel) {
        String token = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .getString("jwt_token", null);

        if (token == null || token.isEmpty()) {
            Toast.makeText(context, "Bạn cần đăng nhập trước", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        new Thread(() -> {
            QuizDao quizDao = BrainBoxDatabase.getInstance(context).quizDao();

            com.example.prm392_v1.data.entity.Quiz old = quizDao.getById(quizModel.quizId);

            com.example.prm392_v1.data.entity.Quiz entity = new com.example.prm392_v1.data.entity.Quiz();
            entity.quizId = quizModel.quizId;
            entity.quizName = quizModel.quizName;
            entity.description = quizModel.description;
            entity.creatorId = quizModel.creatorId;
            entity.isPublic = quizModel.isPublic;
            entity.createdAt = quizModel.createdAt;

            quizDao.insert(entity);

            fetchAndSaveFlashcards(context, quizModel.quizId, () -> {
                String msg = (old != null)
                        ? "Đã thay thế bản tải xuống trước đó"
                        : "Đã tải xuống quiz";

                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
            });
        }).start();
    }

    private static void fetchAndSaveFlashcards(Context context, int quizId, Runnable onSuccess) {
        ApiService api = RetrofitClient.getApiService(context);
        String filter = "QuizId eq " + quizId;

        api.getFlashcardsByFilter(filter).enqueue(new Callback<ODataResponse<Flashcard>>() {
            @Override
            public void onResponse(Call<ODataResponse<Flashcard>> call,
                                   Response<ODataResponse<Flashcard>> response) {

                if (response.code() == 401) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Bạn cần đăng nhập trước", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    });
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    List<Flashcard> flashcards = response.body().value;

                    new Thread(() -> {
                        FlashcardDao dao = BrainBoxDatabase.getInstance(context).flashcardDao();
                        dao.deleteByQuizId(quizId);

                        for (com.example.prm392_v1.data.model.Flashcard f : flashcards) {
                            com.example.prm392_v1.data.entity.Flashcard flashcard = new com.example.prm392_v1.data.entity.Flashcard();
                            flashcard.cardId = f.cardId;
                            flashcard.quizId = f.quizId;
                            flashcard.question = f.question;
                            flashcard.option1 = f.option1;
                            flashcard.option2 = f.option2;
                            flashcard.option3 = f.option3;
                            flashcard.option4 = f.option4;
                            flashcard.answer = f.answer;
                            dao.insert(flashcard);
                        }
                        onSuccess.run();
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<com.example.prm392_v1.data.model.Flashcard>> call, Throwable t) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Lỗi tải flashcards", Toast.LENGTH_SHORT).show());
            }
        });
    }

}
