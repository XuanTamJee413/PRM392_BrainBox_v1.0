package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestModeActivity extends AppCompatActivity {

    private TextView textQuizTitle, textFinalTestResult;
    private RecyclerView recyclerViewTestQuestions;
    private Button buttonSubmitTest, buttonBack;
    private com.example.prm392_v1.ui.main.QuestionAdapter testQuestionAdapter; // Using QuestionAdapter for test mode
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
        textQuizTitle.setText(quizName);

        setupRecyclerView();
        setupClickListeners();

        if (quizId != -1) {
            fetchFlashcards(quizId);
        }
    }

    private void initializeViews() {
        textQuizTitle = findViewById(R.id.text_quiz_title);
        recyclerViewTestQuestions = findViewById(R.id.recycler_view_test_questions);
        buttonSubmitTest = findViewById(R.id.button_submit_test);
        buttonBack = findViewById(R.id.button_back);
        textFinalTestResult = findViewById(R.id.text_final_test_result); // Changed ID to reflect test result
    }

    private void setupRecyclerView() {
        recyclerViewTestQuestions.setLayoutManager(new LinearLayoutManager(this));
        // Adapter will be set after fetching data
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
                        testQuestionAdapter = new com.example.prm392_v1.ui.main.QuestionAdapter(TestModeActivity.this, flashcardList);
                        recyclerViewTestQuestions.setAdapter(testQuestionAdapter);
                    } else {
                        Toast.makeText(TestModeActivity.this, "Không có câu hỏi nào cho bài kiểm tra này.", Toast.LENGTH_SHORT).show();
                        buttonSubmitTest.setEnabled(false);
                    }
                } else {
                    Toast.makeText(TestModeActivity.this, "Không thể tải câu hỏi. Lỗi phản hồi.", Toast.LENGTH_SHORT).show();
                    buttonSubmitTest.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(TestModeActivity.this, "Lỗi tải câu hỏi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                buttonSubmitTest.setEnabled(false);
            }
        });
    }

    private void submitTest() {
        if (isTestSubmitted || testQuestionAdapter == null || flashcardList.isEmpty()) {
            Toast.makeText(this, "Không có câu hỏi để nộp bài hoặc bài kiểm tra đã được nộp.", Toast.LENGTH_SHORT).show();
            return;
        }

        int correctCount = 0;
        int totalQuestions = flashcardList.size();
        int[] userAnswers = testQuestionAdapter.getUserAnswers(); // Get answers from the adapter

        for (int i = 0; i < totalQuestions; i++) {
            Flashcard flashcard = flashcardList.get(i);
            int userAnswer = userAnswers[i];
            int correctAnswer = flashcard.answer;

            if (userAnswer == correctAnswer) {
                correctCount++;
            }
        }

        double percentage = (totalQuestions > 0) ? (correctCount * 100.0) / totalQuestions : 0.0;
        String resultText = String.format("Kết quả: %d/%d đúng (%.1f%%)", correctCount, totalQuestions, percentage);

        textFinalTestResult.setText(resultText);
        textFinalTestResult.setVisibility(View.VISIBLE);
        buttonSubmitTest.setVisibility(View.GONE); // Hide submit button

        // Inform the adapter to show feedback and disable interaction
        testQuestionAdapter.setShowFeedback(true);
        isTestSubmitted = true; // Mark test as submitted

        Toast.makeText(this, "Bạn đã nộp bài kiểm tra. Xem kết quả và phản hồi!", Toast.LENGTH_LONG).show();
    }
}