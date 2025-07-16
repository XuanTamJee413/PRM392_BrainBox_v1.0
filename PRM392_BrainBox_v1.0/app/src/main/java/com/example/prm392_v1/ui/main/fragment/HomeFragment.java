package com.example.prm392_v1.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.QuizDto;
import com.example.prm392_v1.data.model.RatingQuiz;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.DocumentAdapter;
import com.example.prm392_v1.ui.adapters.QuizAdapter;
import com.example.prm392_v1.ui.auth.LoginActivity;
import com.example.prm392_v1.ui.main.DocumentDetailActivity;
import com.example.prm392_v1.ui.main.PurchaseActivity;
import com.example.prm392_v1.ui.main.QuizActivity;
import com.example.prm392_v1.ui.main.QuizDetailActivity;
import com.example.prm392_v1.ui.main.ViewDocumentActivity;
import com.example.prm392_v1.ui.main.ViewDocumentDetailActivity;
import com.example.prm392_v1.utils.DocumentDownloader;
import com.example.prm392_v1.utils.QuizDownloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerDocuments, recyclerQuizzes;
    private View rootView;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setupUpgradeButtons();
        setupSeeAllButtons();
        setupRecyclerViews();
        fetchTopDocuments();
        fetchLatestQuizzes();

        return rootView;
    }

    private void setupUpgradeButtons() {
        int[] buttonIds = {
                R.id.btn_upgrade_lifetime,
                R.id.btn_upgrade_30days,
                R.id.btn_upgrade_6months,
                R.id.btn_upgrade_12months
        };

        View.OnClickListener listener = v -> {
            String token = requireContext()
                    .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    .getString("jwt_token", null);

            if (token == null || token.isEmpty()) {
                Toast.makeText(requireContext(), "Bạn phải đăng nhập trước", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }

            String selectedPackage = "";
            int id = v.getId();
            if (id == R.id.btn_upgrade_lifetime) {
                selectedPackage = "lifetime";
            } else if (id == R.id.btn_upgrade_30days) {
                selectedPackage = "30days";
            } else if (id == R.id.btn_upgrade_6months) {
                selectedPackage = "6months";
            } else if (id == R.id.btn_upgrade_12months) {
                selectedPackage = "12months";
            }

            Intent intent = new Intent(requireContext(), PurchaseActivity.class);
            intent.putExtra("selected_package", selectedPackage);
            startActivity(intent);
        };

        for (int id : buttonIds) {
            rootView.findViewById(id).setOnClickListener(listener);
        }
    }

    private void setupSeeAllButtons() {
        rootView.findViewById(R.id.text_see_all_docs).setOnClickListener(v -> {
            ViewDocumentActivity.start(requireContext());
        });
        rootView.findViewById(R.id.text_see_all_quizzes).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), QuizActivity.class));
        });
    }

    private void setupRecyclerViews() {
        recyclerDocuments = rootView.findViewById(R.id.recycler_top_documents);
        recyclerQuizzes = rootView.findViewById(R.id.recycler_latest_quizzes);

        recyclerDocuments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerQuizzes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void fetchTopDocuments() {
        String token = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .getString("jwt_token", null);
        if (token == null || token.isEmpty()) {
            showErrorText(R.id.text_no_documents, "Vui lòng đăng nhập để tải tài liệu.");
            Toast.makeText(requireContext(), "Bạn phải đăng nhập trước", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            return;
        }

        ApiService apiService = RetrofitClient.getApiService(requireContext());
        Call<ODataResponse<DocumentDto>> call = apiService.getAllDocuments(null, "Author");
        Log.d(TAG, "Request URL: " + call.request().url());

        call.enqueue(new Callback<ODataResponse<DocumentDto>>() {
            @Override
            public void onResponse(Call<ODataResponse<DocumentDto>> call, Response<ODataResponse<DocumentDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DocumentDto> docs = response.body().value;
                    Log.d(TAG, "Fetched " + (docs != null ? docs.size() : 0) + " documents");

                    // Sort by Views in descending order and limit to 5
                    List<DocumentDto> topDocs = docs != null ? new ArrayList<>(docs) : new ArrayList<>();
                    if (!topDocs.isEmpty()) {
                        Collections.sort(topDocs, (doc1, doc2) -> Integer.compare(doc2.Views, doc1.Views));
                        topDocs = topDocs.size() > 5 ? topDocs.subList(0, 5) : topDocs;
                    }

                    showOrHideRecycler(recyclerDocuments, R.id.text_no_documents, topDocs);

                    if (topDocs != null && !topDocs.isEmpty()) {
                        DocumentAdapter adapter = new DocumentAdapter();
                        adapter.submitList(topDocs);
                        recyclerDocuments.setAdapter(adapter);
                        adapter.setOnItemClickListener(document -> {
                            Bundle bundle = new Bundle();
                            bundle.putInt("docId", document.DocId);
                            bundle.putString("title", document.Title); // Pass title for display
                            ViewDocumentDetailActivity.start(requireContext(), bundle);
                        });
                        adapter.setOnDownloadClickListener(document -> {
                            DocumentDownloader.downloadDocumentWithDetails(requireContext(), document);
                        });
                    }
                } else {
                    Log.e(TAG, "Failed to fetch documents. Code: " + response.code() + ", Message: " + response.message());
                    try {
                        Log.e(TAG, "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e(TAG, "Could not parse error body: " + e.getMessage());
                    }
                    showErrorText(R.id.text_no_documents, "Không tải được dữ liệu. Mã lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<DocumentDto>> call, Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
                showErrorText(R.id.text_no_documents, "Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    private void fetchLatestQuizzes() {
        ApiService apiService = RetrofitClient.getApiService(requireContext());
        apiService.getLatestQuizzes(null, "CreatedAt desc", 5, "Creator($select=Username)")
                .enqueue(new Callback<ODataResponse<QuizDto>>() {
                    @Override
                    public void onResponse(Call<ODataResponse<QuizDto>> call, Response<ODataResponse<QuizDto>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<QuizDto> quizDtos = response.body().value;
                            showOrHideRecycler(recyclerQuizzes, R.id.text_no_quizzes, quizDtos);

                            if (quizDtos != null && !quizDtos.isEmpty()) {
                                List<Quiz> quizzes = new ArrayList<>();
                                for (QuizDto dto : quizDtos) {
                                    Quiz quiz = new Quiz();
                                    quiz.quizId = dto.QuizId;
                                    quiz.quizName = dto.QuizName;
                                    quiz.description = dto.Description;
                                    quiz.flashcards = new ArrayList<>();
                                    quizzes.add(quiz);
                                }

                                fetchFlashcardsAndRatingsForQuizzes(quizzes);
                            } else {
                                showErrorText(R.id.text_no_quizzes, "Không có quiz nào.");
                            }
                        } else {
                            Log.e(TAG, "Failed to fetch quizzes. Code: " + response.code() + ", Message: " + response.message());
                            showErrorText(R.id.text_no_quizzes, "Không tải được dữ liệu. Mã lỗi: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ODataResponse<QuizDto>> call, Throwable t) {
                        Log.e(TAG, "API call failed: " + t.getMessage(), t);
                        showErrorText(R.id.text_no_quizzes, "Lỗi kết nối: " + t.getMessage());
                    }
                });
    }

    private void fetchFlashcardsAndRatingsForQuizzes(List<Quiz> quizzes) {
        if (quizzes.isEmpty()) {
            recyclerQuizzes.setVisibility(View.GONE);
            rootView.findViewById(R.id.text_no_quizzes).setVisibility(View.VISIBLE);
            Log.d(TAG, "fetchFlashcardsAndRatingsForQuizzes called with empty list.");
            return;
        }

        final CountDownLatch latch = new CountDownLatch(quizzes.size() * 2);
        ApiService apiService = RetrofitClient.getApiService(requireContext());
        Log.d(TAG, "Starting to fetch flashcards and ratings for " + quizzes.size() + " quizzes.");

        for (Quiz quiz : quizzes) {
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
                        Toast.makeText(requireContext(), "Data loading interrupted, some data may be incomplete.",
                                Toast.LENGTH_LONG).show();
                    });
                }
            } catch (InterruptedException e) {
                Log.e(TAG, "Latch await interrupted: " + e.getMessage());
                Thread.currentThread().interrupt();
            } finally {
                new Handler(Looper.getMainLooper()).post(() -> {
                    QuizAdapter adapter = new QuizAdapter();
                    adapter.submitList(new ArrayList<>(quizzes));
                    recyclerQuizzes.setAdapter(adapter);
                    adapter.setOnItemClickListener(quiz -> {
                        Intent intent = new Intent(requireContext(), QuizDetailActivity.class);
                        intent.putExtra("EXTRA_QUIZ_ID", quiz.quizId);
                        intent.putExtra("EXTRA_QUIZ_NAME", quiz.quizName);
                        startActivity(intent);
                    });
                    adapter.setOnDownloadClickListener(quiz -> {
                        QuizDownloader.downloadQuizWithFlashcards(requireContext(), quiz);
                    });
                    for (Quiz quiz : quizzes) {
                        Log.d(TAG, "Quiz: " + quiz.quizName + ", quizId: " + quiz.quizId +
                                ", avgRating: " + quiz.averageRating +
                                ", totalRatings: " + quiz.totalRatings +
                                ", flashcardCount: " + (quiz.flashcards != null ? quiz.flashcards.size() : 0));
                    }
                    Log.d(TAG, "Quiz adapter updated with " + quizzes.size() + " quizzes.");
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

    private <T> void showOrHideRecycler(RecyclerView recyclerView, int emptyViewId, List<T> list) {
        if (list == null || list.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            rootView.findViewById(emptyViewId).setVisibility(View.VISIBLE);
            ((TextView) rootView.findViewById(emptyViewId)).setText("Không có tài liệu nào.");
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            rootView.findViewById(emptyViewId).setVisibility(View.GONE);
        }
    }

    private void showErrorText(int viewId, String message) {
        TextView textView = rootView.findViewById(viewId);
        recyclerDocuments.setVisibility(View.GONE); // Updated to hide documents RecyclerView
        textView.setVisibility(View.VISIBLE);
        textView.setText(message);
    }
}