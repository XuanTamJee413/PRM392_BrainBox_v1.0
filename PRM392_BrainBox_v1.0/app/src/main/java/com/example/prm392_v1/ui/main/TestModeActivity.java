package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestModeActivity extends AppCompatActivity {

    private static final String TAG = "TestModeActivity";
    private TextView textQuizTitle, textFinalTestResult;
    private RecyclerView recyclerViewTestQuestions;
    private Button buttonSubmitTest, buttonBack;
    private QuestionAdapter testQuestionAdapter;
    private List<Flashcard> flashcardList = new ArrayList<>();
    private int quizId;
    private boolean isTestSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);

        quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");

        initializeViews();
        textQuizTitle.setText(quizName != null ? quizName : "Quiz");

        setupRecyclerView();
        setupClickListeners();

        if (quizId != -1) {
            fetchFlashcards(quizId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID bài kiểm tra.", Toast.LENGTH_LONG).show();
            buttonSubmitTest.setEnabled(false);
        }
    }

    private void initializeViews() {
        textQuizTitle = findViewById(R.id.text_quiz_title);
        recyclerViewTestQuestions = findViewById(R.id.recycler_view_test_questions);
        buttonSubmitTest = findViewById(R.id.button_submit_test);
        buttonBack = findViewById(R.id.button_back);
        textFinalTestResult = findViewById(R.id.text_final_test_result);
    }

    private void setupRecyclerView() {
        recyclerViewTestQuestions.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupClickListeners() {
        buttonSubmitTest.setOnClickListener(v -> submitTest());
        buttonBack.setOnClickListener(v -> finish());
    }

    private void fetchFlashcards(int quizId) {
        ApiService apiService = RetrofitClient.getApiService(this);
        String filter = "QuizId eq " + quizId;

        apiService.getFlashcardsByFilter(filter).enqueue(new Callback<ODataResponse<Flashcard>>() {
            @Override
            public void onResponse(Call<ODataResponse<Flashcard>> call, Response<ODataResponse<Flashcard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    flashcardList = response.body().value;
                    if (!flashcardList.isEmpty()) {
                        // Validate flashcards
                        boolean isValid = true;
                        for (Flashcard flashcard : flashcardList) {
                            if (flashcard.answer < 1 || flashcard.answer > 4 ||
                                    flashcard.question == null || flashcard.option1 == null ||
                                    flashcard.option2 == null || flashcard.option3 == null ||
                                    flashcard.option4 == null) {
                                isValid = false;
                                Log.e(TAG, "Invalid flashcard data for cardId: " + flashcard.cardId);
                                break;
                            }
                        }
                        if (isValid) {
                            testQuestionAdapter = new QuestionAdapter(TestModeActivity.this, flashcardList);
                            recyclerViewTestQuestions.setAdapter(testQuestionAdapter);
                            Log.d(TAG, "Loaded " + flashcardList.size() + " valid flashcards for quizId: " + quizId);
                        } else {
                            Toast.makeText(TestModeActivity.this, "Dữ liệu câu hỏi không hợp lệ.", Toast.LENGTH_LONG).show();
                            buttonSubmitTest.setEnabled(false);
                        }
                    } else {
                        Toast.makeText(TestModeActivity.this, "Không có câu hỏi nào cho bài kiểm tra này.", Toast.LENGTH_SHORT).show();
                        buttonSubmitTest.setEnabled(false);
                    }
                } else {
                    Toast.makeText(TestModeActivity.this, "Không thể tải câu hỏi. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    buttonSubmitTest.setEnabled(false);
                    Log.e(TAG, "Failed to fetch flashcards. Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(TestModeActivity.this, "Lỗi tải câu hỏi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                buttonSubmitTest.setEnabled(false);
                Log.e(TAG, "API call failed: " + t.getMessage(), t);
            }
        });
    }

    private void submitTest() {
        if (isTestSubmitted || testQuestionAdapter == null || flashcardList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi để nộp bài hoặc bài kiểm tra đã được nộp.", Toast.LENGTH_SHORT).show();
            return;
        }

        int[] userAnswers = testQuestionAdapter.getUserAnswers();
        int correctCount = 0;
        int totalQuestions = flashcardList.size();
        int answeredQuestions = 0;

        // Log to debug user answers and correct answers
        Log.d(TAG, "Submitting test. Total questions: " + totalQuestions);
        for (int i = 0; i < totalQuestions; i++) {
            int userAnswer = userAnswers[i];
            int correctAnswer = flashcardList.get(i).answer;
            Log.d(TAG, "Question " + (i + 1) + ": User answer = " + userAnswer + ", Correct answer = " + correctAnswer);
            if (userAnswer != 0) { // Count answered questions
                answeredQuestions++;
                if (userAnswer == correctAnswer) {
                    correctCount++;
                }
            }
        }

        // Calculate percentage
        double percentage = (totalQuestions > 0) ? (correctCount * 100.0) / totalQuestions : 0.0;
        String resultText = String.format("Kết quả: %d/%d đúng (%.1f%%) - Đã trả lời: %d/%d",
                correctCount, totalQuestions, percentage, answeredQuestions, totalQuestions);

        // Update UI
        textFinalTestResult.setText(resultText);
        textFinalTestResult.setVisibility(View.VISIBLE);

        // Color-code result based on performance
        if (percentage >= 80) {
            textFinalTestResult.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark));
        } else if (percentage >= 50) {
            textFinalTestResult.setTextColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
        } else {
            textFinalTestResult.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
        }

        // Hide submit button and show feedback
        buttonSubmitTest.setVisibility(View.GONE);
        testQuestionAdapter.setShowFeedback(true);
        testQuestionAdapter.notifyDataSetChanged(); // Ensure adapter refreshes to show feedback
        isTestSubmitted = true;

        Toast.makeText(this, "Bạn đã nộp bài kiểm tra. Xem kết quả và phản hồi!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Test submitted. Result: " + resultText);
    }
}