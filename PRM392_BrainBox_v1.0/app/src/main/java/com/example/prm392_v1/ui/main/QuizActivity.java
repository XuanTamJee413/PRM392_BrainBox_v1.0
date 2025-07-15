package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.RatingQuiz;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.QuizAdapter;
import com.example.prm392_v1.ui.main.fragment.ChatAiDialogFragment;
import com.example.prm392_v1.ui.views.DraggableFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity implements QuizAdapter.OnItemClickListener {

    private static final String TAG = "QuizActivity";

    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private List<Quiz> fullQuizList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // --- BẮT ĐẦU PHẦN THÊM MỚI ---
        // Tìm icon AI nổi và thiết lập sự kiện click
        DraggableFloatingActionButton fabAi = findViewById(R.id.fab_ai_assistant);
        fabAi.setOnClickListener(view -> {
            // Tạo một instance của DialogFragment chat
            ChatAiDialogFragment dialogFragment = new ChatAiDialogFragment();
            // Hiển thị dialog
            dialogFragment.show(getSupportFragmentManager(), "ChatAiDialog");
        });
        // --- KẾT THÚC PHẦN THÊM MỚI ---

        progressBar = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        Button btnBackHome = findViewById(R.id.btn_back_home);

        apiService = RetrofitClient.getApiService(this);

        setupRecyclerView();
        setupSearchView();

        btnBackHome.setOnClickListener(v -> finish());

        loadQuizzesFromApi();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.quiz_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizAdapter = new QuizAdapter();
        recyclerView.setAdapter(quizAdapter);
        quizAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(Quiz quiz) {
        Intent intent = new Intent(this, QuizDetailActivity.class);
        intent.putExtra("EXTRA_QUIZ_ID", quiz.quizId);
        intent.putExtra("EXTRA_QUIZ_NAME", quiz.quizName);
        startActivity(intent);
    }

    private void loadQuizzesFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Starting to load quizzes from API.");

        apiService.getAllQuizzes().enqueue(new Callback<ODataResponse<Quiz>>() {
            @Override
            public void onResponse(Call<ODataResponse<Quiz>> call, Response<ODataResponse<Quiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullQuizList = response.body().value;
                    Log.d(TAG, "Successfully loaded " + fullQuizList.size() + " quizzes.");

                    if (!fullQuizList.isEmpty()) {
                        fetchFlashcardsAndRatingsForQuizzes(fullQuizList);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(QuizActivity.this, "No quizzes found.", Toast.LENGTH_SHORT).show();
                        quizAdapter.submitList(new ArrayList<>());
                        Log.d(TAG, "No quizzes found. Adapter updated with empty list.");
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(QuizActivity.this, "Failed to load quizzes. Error code: " + response.code(), Toast.LENGTH_SHORT).show();
                    quizAdapter.submitList(new ArrayList<>());
                    Log.e(TAG, "Failed to load quizzes. Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Quiz>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(QuizActivity.this, "Connection error while loading quizzes: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                quizAdapter.submitList(new ArrayList<>());
                Log.e(TAG, "API call getAllQuizzes failed: " + t.getMessage(), t);
            }
        });
    }

    private void fetchFlashcardsAndRatingsForQuizzes(List<Quiz> quizzes) {
        if (quizzes.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            quizAdapter.submitList(new ArrayList<>(fullQuizList));
            Log.d(TAG, "fetchFlashcardsAndRatingsForQuizzes called with empty list. Updating adapter.");
            return;
        }

        final CountDownLatch latch = new CountDownLatch(quizzes.size() * 2);
        Log.d(TAG, "Starting to fetch flashcards and ratings for " + quizzes.size() + " quizzes.");

        for (Quiz quiz : quizzes) {
            // Fetch flashcards
            String filter = "QuizId eq " + quiz.quizId;
            apiService.getFlashcardsForQuiz(filter).enqueue(new Callback<ODataResponse<Flashcard>>() {
                @Override
                public void onResponse(Call<ODataResponse<Flashcard>> call, Response<ODataResponse<Flashcard>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        quiz.flashcards = response.body().value;
                        Log.d(TAG, "Fetched " + (quiz.flashcards != null ? quiz.flashcards.size() : 0) +
                                " flashcards for quizId: " + quiz.quizId);
                    } else {
                        quiz.flashcards = new ArrayList<>();
                        Log.w(TAG, "Failed to fetch flashcards for quizId " + quiz.quizId +
                                ". Code: " + response.code() + ", Message: " + response.message());
                    }
                    latch.countDown();
                }

                @Override
                public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                    quiz.flashcards = new ArrayList<>();
                    Log.e(TAG, "API call for flashcards for quizId " + quiz.quizId + " failed: " + t.getMessage(), t);
                    latch.countDown();
                }
            });

            // Fetch ratings
            apiService.getRatingsForQuiz(quiz.quizId).enqueue(new Callback<List<RatingQuiz>>() {
                @Override
                public void onResponse(Call<List<RatingQuiz>> call, Response<List<RatingQuiz>> response) {
                    Log.d(TAG, "Raw response for ratings quizId " + quiz.quizId + ": " + response.body());
                    if (response.isSuccessful() && response.body() != null) {
                        List<RatingQuiz> ratings = response.body();
                        Log.d(TAG, "Fetched " + ratings.size() + " ratings for quizId: " + quiz.quizId);
                        for (RatingQuiz rating : ratings) {
                            Log.d(TAG, "Rating: quizId=" + rating.quizId + ", rating=" + rating.rating +
                                    ", comment=" + (rating.comment != null ? rating.comment : "null"));
                        }
                        calculateAndSetAverageRating(quiz, ratings);
                    } else if (response.code() == 404) {
                        quiz.averageRating = 0.0f;
                        quiz.totalRatings = 0;
                        Log.d(TAG, "No ratings found for quizId: " + quiz.quizId + " (HTTP 404)");
                    } else {
                        quiz.averageRating = 0.0f;
                        quiz.totalRatings = 0;
                        Log.e(TAG, "Failed to fetch ratings for quizId " + quiz.quizId +
                                ". Code: " + response.code() + ", Message: " + response.message());
                    }
                    latch.countDown();
                }

                @Override
                public void onFailure(Call<List<RatingQuiz>> call, Throwable t) {
                    quiz.averageRating = 0.0f;
                    quiz.totalRatings = 0;
                    Log.e(TAG, "API call for ratings for quizId " + quiz.quizId + " failed: " + t.getMessage(), t);
                    latch.countDown();
                }
            });
        }

        new Thread(() -> {
            try {
                boolean allDone = latch.await(10, TimeUnit.SECONDS);
                if (!allDone) {
                    Log.w(TAG, "Timeout while fetching flashcards or ratings. Data may be incomplete.");
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(QuizActivity.this, "Data loading interrupted, some data may be incomplete.",
                                Toast.LENGTH_LONG).show();
                    });
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Latch await interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            } finally {
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    quizAdapter.submitList(new ArrayList<>(fullQuizList));
                    for (Quiz quiz : fullQuizList) {
                        Log.d(TAG, "Quiz: " + quiz.quizName + ", quizId: " + quiz.quizId +
                                ", avgRating: " + quiz.averageRating +
                                ", totalRatings: " + quiz.totalRatings +
                                ", flashcardCount: " + (quiz.flashcards != null ? quiz.flashcards.size() : 0));
                    }
                    Log.d(TAG, "Adapter updated with " + fullQuizList.size() + " quizzes.");
                });
            }
        }).start();
    }

    private void calculateAndSetAverageRating(Quiz quiz, List<RatingQuiz> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            quiz.averageRating = 0.0f;
            quiz.totalRatings = 0;
            Log.d(TAG, "No ratings for quizId: " + quiz.quizId + ", setting averageRating to 0.0, totalRatings to 0");
            return;
        }

        float totalRating = 0;
        int validRatingsCount = 0;
        for (RatingQuiz rating : ratings) {
            if (rating != null && rating.rating >= 0 && rating.rating <= 5) {
                totalRating += rating.rating;
                validRatingsCount++;
                Log.d(TAG, "Processed rating for quizId: " + quiz.quizId + ", rating value: " + rating.rating +
                        ", comment: " + (rating.comment != null ? rating.comment : "null"));
            } else {
                Log.w(TAG, "Invalid rating for quizId: " + quiz.quizId + ", rating: " +
                        (rating != null ? rating.rating : "null"));
            }
        }

        if (validRatingsCount > 0) {
            quiz.averageRating = totalRating / validRatingsCount;
            quiz.totalRatings = validRatingsCount;
        } else {
            quiz.averageRating = 0.0f;
            quiz.totalRatings = 0;
        }
        Log.d(TAG, "Calculated for quizId: " + quiz.quizId + ", averageRating: " + quiz.averageRating +
                ", totalRatings: " + quiz.totalRatings + ", totalRatingSum: " + totalRating);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        searchView.clearFocus();
    }

    private void filter(String text) {
        List<Quiz> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(fullQuizList);
        } else {
            for (Quiz item : fullQuizList) {
                if (item.quizName.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        quizAdapter.submitList(new ArrayList<>(filteredList));
        Log.d(TAG, "Filtered list updated. Size: " + filteredList.size());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (searchView.hasFocus()) {
                int[] location = new int[2];
                searchView.getLocationOnScreen(location);
                int left = location[0];
                int top = location[1];
                int right = left + searchView.getWidth();
                int bottom = top + searchView.getHeight();

                float x = event.getRawX();
                float y = event.getRawY();
                if (x < left || x > right || y < top || y > bottom) {
                    searchView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}