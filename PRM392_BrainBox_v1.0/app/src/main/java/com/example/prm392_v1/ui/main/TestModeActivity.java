package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestModeActivity extends AppCompatActivity {

    private TextView textQuizTitle, textQuestion, textResult;
    private Button buttonOptionA, buttonOptionB, buttonOptionC, buttonOptionD, buttonSubmit, buttonBack;
    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private Map<Integer, Integer> userAnswers = new HashMap<>();
    private boolean isSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mode);

        int quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");

        initializeViews();
        textQuizTitle.setText(quizName);

        setupClickListeners();

        if (quizId != -1) {
            fetchFlashcards(quizId);
        }
    }

    private void initializeViews() {
        textQuizTitle = findViewById(R.id.text_quiz_title);
        textQuestion = findViewById(R.id.text_question);
        textResult = findViewById(R.id.text_result);
        buttonOptionA = findViewById(R.id.button_option_a);
        buttonOptionB = findViewById(R.id.button_option_b);
        buttonOptionC = findViewById(R.id.button_option_c);
        buttonOptionD = findViewById(R.id.button_option_d);
        buttonSubmit = findViewById(R.id.button_submit);
        buttonBack = findViewById(R.id.button_back);
    }

    private void setupClickListeners() {
        buttonOptionA.setOnClickListener(v -> selectAnswer(1));
        buttonOptionB.setOnClickListener(v -> selectAnswer(2));
        buttonOptionC.setOnClickListener(v -> selectAnswer(3));
        buttonOptionD.setOnClickListener(v -> selectAnswer(4));

        buttonSubmit.setOnClickListener(v -> submitTest());

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
                        updateQuestionView();
                    } else {
                        textQuestion.setText("Không có câu hỏi nào.");
                        disableOptions();
                    }
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(TestModeActivity.this, "Lỗi tải câu hỏi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuestionView() {
        if (flashcardList.isEmpty() || isSubmitted) return;
        Flashcard currentCard = flashcardList.get(currentCardIndex);
        textQuestion.setText(currentCard.question);
        buttonOptionA.setText("A. " + currentCard.option1);
        buttonOptionB.setText("B. " + currentCard.option2);
        buttonOptionC.setText("C. " + currentCard.option3);
        buttonOptionD.setText("D. " + currentCard.option4);
        textResult.setVisibility(View.GONE);
        enableOptions();
        Integer userAnswer = userAnswers.get(currentCardIndex);
        if (userAnswer != null) {
            selectAnswer(userAnswer); // Pre-select if answered
        }
    }

    private void selectAnswer(int answer) {
        if (isSubmitted) return;
        Flashcard currentCard = flashcardList.get(currentCardIndex);
        userAnswers.put(currentCardIndex, answer);
        disableOptions();
    }

    private void submitTest() {
        if (isSubmitted || flashcardList.isEmpty()) return;
        int correctCount = 0;
        for (int i = 0; i < flashcardList.size(); i++) {
            Flashcard card = flashcardList.get(i);
            Integer userAnswer = userAnswers.get(i);
            if (userAnswer != null && userAnswer == card.answer) {
                correctCount++;
            }
        }
        textResult.setText(String.format("Kết quả: %d/%d đúng (%.1f%%)", correctCount, flashcardList.size(), (correctCount * 100.0) / flashcardList.size()));
        textResult.setVisibility(View.VISIBLE);
        isSubmitted = true;
        disableOptions();
        buttonSubmit.setVisibility(View.GONE);
    }

    private void enableOptions() {
        buttonOptionA.setEnabled(true);
        buttonOptionB.setEnabled(true);
        buttonOptionC.setEnabled(true);
        buttonOptionD.setEnabled(true);
    }

    private void disableOptions() {
        buttonOptionA.setEnabled(false);
        buttonOptionB.setEnabled(false);
        buttonOptionC.setEnabled(false);
        buttonOptionD.setEnabled(false);
    }
}