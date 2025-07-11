package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class MatchModeActivity extends AppCompatActivity {

    private TextView textQuizTitle, textInstructions, textResult;
    private LinearLayout leftContainer, rightContainer;
    private Button buttonCheck, buttonBack;
    private List<Flashcard> flashcardList = new ArrayList<>();
    private Map<Integer, Integer> userMatches = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_mode);

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
        textInstructions = findViewById(R.id.text_instructions);
        textResult = findViewById(R.id.text_result);
        leftContainer = findViewById(R.id.left_container);
        rightContainer = findViewById(R.id.right_container);
        buttonCheck = findViewById(R.id.button_check);
        buttonBack = findViewById(R.id.button_back);
    }

    private void setupClickListeners() {
        buttonCheck.setOnClickListener(v -> checkMatches());
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
                        setupMatchGame();
                    } else {
                        textInstructions.setText("Không có flashcard nào.");
                    }
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(MatchModeActivity.this, "Lỗi tải flashcard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupMatchGame() {
        // This is a placeholder. Implement drag-and-drop or click-based matching.
        // For simplicity, assume manual pairing (to be enhanced with drag-and-drop logic).
        for (int i = 0; i < flashcardList.size(); i++) {
            Flashcard card = flashcardList.get(i);
            TextView leftItem = new TextView(this);
            leftItem.setText(card.question);
            leftContainer.addView(leftItem);

            TextView rightItem = new TextView(this);
            String correctAnswer = getCorrectOption(card);
            rightItem.setText(correctAnswer);
            rightContainer.addView(rightItem);

            // Simulate user matching (replace with actual UI interaction)
            userMatches.put(i, i); // Initial state, to be updated by user
        }
    }

    private String getCorrectOption(Flashcard card) {
        String[] options = {card.option1, card.option2, card.option3, card.option4};
        return (card.answer >= 1 && card.answer <= 4) ? options[card.answer - 1] : "Không có đáp án hợp lệ.";
    }

    private void checkMatches() {
        int correctCount = 0;
        for (int i = 0; i < flashcardList.size(); i++) {
            if (userMatches.get(i) != null && userMatches.get(i) == i) {
                correctCount++;
            }
        }
        textResult.setText(String.format("Kết quả: %d/%d cặp đúng (%.1f%%)", correctCount, flashcardList.size(), (correctCount * 100.0) / flashcardList.size()));
        textResult.setVisibility(View.VISIBLE);
    }
}