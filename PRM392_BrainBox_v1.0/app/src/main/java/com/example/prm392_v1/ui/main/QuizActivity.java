package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log; // Import Log
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit; // Import TimeUnit

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity implements QuizAdapter.OnItemClickListener {

    private static final String TAG = "QuizActivity"; // Tag cho Logcat

    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private List<Quiz> fullQuizList = new ArrayList<>(); // Danh sách này sẽ chứa quiz với thông tin rating đã cập nhật
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        progressBar = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        Button btnBackHome = findViewById(R.id.btn_back_home);

        apiService = RetrofitClient.getApiService(this); // Khởi tạo ApiService

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
        // Để truyền danh sách flashcard, Quiz và Flashcard phải implement Parcelable hoặc Serializable.
        // Nếu API getAllQuizzes của bạn đã trả về flashcards, bạn có thể truyền chúng đi:
        // if (quiz.flashcards != null && !quiz.flashcards.isEmpty()) {
        //     // Đảm bảo Flashcard model của bạn implement Parcelable/Serializable
        //     intent.putExtra("EXTRA_QUIZ_FLASHCARDS", new ArrayList<>(quiz.flashcards));
        // }
        startActivity(intent);
    }

    private void loadQuizzesFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "Bắt đầu tải danh sách quiz từ API.");

        apiService.getAllQuizzes().enqueue(new Callback<ODataResponse<Quiz>>() {
            @Override
            public void onResponse(Call<ODataResponse<Quiz>> call, Response<ODataResponse<Quiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullQuizList = response.body().value;
                    Log.d(TAG, "Tải thành công " + fullQuizList.size() + " quiz.");

                    if (!fullQuizList.isEmpty()) {
                        // Fetch flashcards and ratings for quizzes
                        fetchFlashcardsAndRatingsForQuizzes(fullQuizList);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(QuizActivity.this, "Không có quiz nào để hiển thị.", Toast.LENGTH_SHORT).show();
                        quizAdapter.submitList(new ArrayList<>());
                        Log.d(TAG, "Không tìm thấy quiz nào. Adapter được cập nhật với danh sách rỗng.");
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(QuizActivity.this, "Không thể tải dữ liệu quiz. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    quizAdapter.submitList(new ArrayList<>());
                    Log.e(TAG, "Lỗi tải quiz. Mã lỗi: " + response.code() + ", Thông báo: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Quiz>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(QuizActivity.this, "Lỗi kết nối khi tải quiz: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                quizAdapter.submitList(new ArrayList<>());
                Log.e(TAG, "API call getAllQuizzes thất bại: " + t.getMessage(), t);
            }
        });
    }
    private void fetchFlashcardsAndRatingsForQuizzes(List<Quiz> quizzes) {
        if (quizzes.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            quizAdapter.submitList(new ArrayList<>(fullQuizList));
            Log.d(TAG, "fetchFlashcardsAndRatingsForQuizzes called with empty list. Updating adapter with current fullQuizList.");
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
                    Log.d(TAG, "Latch count after flashcards response for quizId " + quiz.quizId + ": " + latch.getCount());
                }

                @Override
                public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                    quiz.flashcards = new ArrayList<>();
                    Log.e(TAG, "API call for flashcards for quizId " + quiz.quizId + " failed: " + t.getMessage(), t);
                    latch.countDown();
                    Log.d(TAG, "Latch count after flashcards failure for quizId " + quiz.quizId + ": " + latch.getCount());
                }
            });

            // Fetch ratings
            apiService.getRatingsForQuiz(quiz.quizId).enqueue(new Callback<List<RatingQuiz>>() {
                @Override
                public void onResponse(Call<List<RatingQuiz>> call, Response<List<RatingQuiz>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<RatingQuiz> ratings = response.body();
                        Log.d(TAG, "Fetched " + ratings.size() + " ratings for quizId: " + quiz.quizId);
                        for (RatingQuiz rating : ratings) {
                            Log.d(TAG, "Rating details - quizId: " + rating.quizId +
                                    ", rating: " + rating.rating + ", comment: " +
                                    (rating.comment != null ? rating.comment : "null") +
                                    ", ratedAt: " + rating.ratedAt);
                        }
                        calculateAndSetAverageRating(quiz, ratings);
                    } else if (response.code() == 404) {
                        // Handle 404 as a valid case: no ratings exist
                        quiz.averageRating = 0.0f;
                        quiz.totalRatings = 0;
                        Log.d(TAG, "No ratings found for quizId: " + quiz.quizId + " (HTTP 404, expected for unrated quizzes)");
                    } else {
                        // Handle unexpected HTTP errors
                        quiz.averageRating = 0.0f;
                        quiz.totalRatings = 0;
                        Log.e(TAG, "Unexpected error fetching ratings for quizId " + quiz.quizId +
                                ". Code: " + response.code() + ", Message: " + response.message());
                    }
                    latch.countDown();
                    Log.d(TAG, "Latch count after ratings response for quizId " + quiz.quizId + ": " + latch.getCount());
                }

                @Override
                public void onFailure(Call<List<RatingQuiz>> call, Throwable t) {
                    quiz.averageRating = 0.0f;
                    quiz.totalRatings = 0;
                    Log.e(TAG, "API call for ratings for quizId " + quiz.quizId + " failed: " + t.getMessage(), t);
                    latch.countDown();
                    Log.d(TAG, "Latch count after ratings failure for quizId " + quiz.quizId + ": " + latch.getCount());
                }
            });
        }

        new Thread(() -> {
            try {
                boolean allDone = latch.await(30, TimeUnit.SECONDS);
                if (!allDone) {
                    Log.w(TAG, "Timeout while fetching flashcards or ratings. Data may be incomplete.");
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(QuizActivity.this, "Tải dữ liệu bị gián đoạn, dữ liệu có thể không đầy đủ.",
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
                    // Log final quiz data for debugging
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
    private void fetchRatingsForQuizzes(List<Quiz> quizzes) {
        if (quizzes.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            quizAdapter.submitList(new ArrayList<>(fullQuizList)); // Cập nhật ngay nếu danh sách rỗng
            Log.d(TAG, "fetchRatingsForQuizzes được gọi với danh sách rỗng. Đang cập nhật fullQuizList hiện tại.");
            return;
        }

        final CountDownLatch latch = new CountDownLatch(quizzes.size());
        Log.d(TAG, "Bắt đầu tải đánh giá cho " + quizzes.size() + " quiz.");

        for (Quiz quiz : quizzes) {
            apiService.getRatingsForQuiz(quiz.quizId).enqueue(new Callback<List<RatingQuiz>>() {
                @Override
                public void onResponse(Call<List<RatingQuiz>> call, Response<List<RatingQuiz>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<RatingQuiz> ratings = response.body();
                        calculateAndSetAverageRating(quiz, ratings);
                        Log.d(TAG, "Tải đánh giá thành công cho quizId: " + quiz.quizId + ". Số lượng: " + ratings.size());
                    } else {
                        // Xử lý lỗi hoặc không có đánh giá, quiz sẽ có rating mặc định 0.0
                        quiz.averageRating = 0.0f;
                        quiz.totalRatings = 0;
                        Log.e(TAG, "Lỗi tải đánh giá cho quizId " + quiz.quizId + ". Mã lỗi: " + response.code() + ", Thông báo: " + response.message());
                    }
                    latch.countDown(); // Giảm số lượng đếm của latch
                    Log.d(TAG, "Latch count sau response cho quizId " + quiz.quizId + ": " + latch.getCount());
                }

                @Override
                public void onFailure(Call<List<RatingQuiz>> call, Throwable t) {
                    // Xử lý lỗi, quiz sẽ có rating mặc định 0.0
                    quiz.averageRating = 0.0f;
                    quiz.totalRatings = 0;
                    Log.e(TAG, "API call tải đánh giá cho quizId " + quiz.quizId + " thất bại: " + t.getMessage(), t);
                    latch.countDown(); // Giảm số lượng đếm của latch ngay cả khi thất bại
                    Log.d(TAG, "Latch count sau failure cho quizId " + quiz.quizId + ": " + latch.getCount());
                }
            });
        }

        // Bắt đầu một luồng mới để chờ latch, tránh block luồng chính (UI)
        new Thread(() -> {
            try {
                // Chờ tất cả các cuộc gọi mạng hoàn thành, với thời gian chờ tối đa
                boolean allDone = latch.await(30, TimeUnit.SECONDS); // Chờ tối đa 30 giây
                if (!allDone) {
                    Log.w(TAG, "Thời gian tải đánh giá đã hết. Một số đánh giá có thể không được tải.");
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(QuizActivity.this, "Tải đánh giá bị gián đoạn, dữ liệu có thể không đầy đủ.", Toast.LENGTH_LONG).show();
                    });
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Latch await bị gián đoạn: " + e.getMessage());
                Thread.currentThread().interrupt(); // Khôi phục trạng thái ngắt
            } finally {
                // Luôn đảm bảo cập nhật UI được đăng lên luồng chính
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    // QUAN TRỌNG: Cần submit một instance mới của List cho DiffUtil để cập nhật đúng cách
                    quizAdapter.submitList(new ArrayList<>(fullQuizList));
                    Log.d(TAG, "Tất cả các lượt tải đánh giá đã hoàn thành (hoặc hết thời gian chờ). Adapter đã được cập nhật.");
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
            if (rating != null && rating.rating >= 0 && rating.rating <= 5) { // Validate rating
                totalRating += rating.rating;
                validRatingsCount++;
                Log.d(TAG, "Rating for quizId: " + quiz.quizId + ", rating value: " + rating.rating +
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
            filteredList.addAll(fullQuizList); // Sử dụng danh sách đầy đủ đã có ratings
        } else {
            for (Quiz item : fullQuizList) {
                if (item.quizName.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        quizAdapter.submitList(new ArrayList<>(filteredList)); // Submit một instance mới
        Log.d(TAG, "Danh sách đã lọc được cập nhật. Kích thước: " + filteredList.size());
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
