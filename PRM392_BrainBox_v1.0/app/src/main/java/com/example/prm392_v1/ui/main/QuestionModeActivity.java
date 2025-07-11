package com.example.prm392_v1.ui.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionModeActivity extends AppCompatActivity {

    private TextView textQuizTitle, textQuestion, textResult;
    private Button buttonOptionA, buttonOptionB, buttonOptionC, buttonOptionD, buttonNext, buttonBack;
    private List<Flashcard> flashcardList = new ArrayList<>();
    private List<Integer> retryIndices = new ArrayList<>(); // Track indices to retry
    private Set<Integer> correctAnswers = new HashSet<>(); // Track all correct answers
    private int currentCardIndex = 0;
    private boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_mode);

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
        buttonNext = findViewById(R.id.button_next);
        buttonBack = findViewById(R.id.button_back);
    }

    private void setupClickListeners() {
        buttonOptionA.setOnClickListener(v -> checkAnswer(1));
        buttonOptionB.setOnClickListener(v -> checkAnswer(2));
        buttonOptionC.setOnClickListener(v -> checkAnswer(3));
        buttonOptionD.setOnClickListener(v -> checkAnswer(4));

        buttonNext.setOnClickListener(v -> {
            if (!retryIndices.isEmpty()) {
                currentCardIndex = retryIndices.remove(0); // Move to next retry index
            } else if (currentCardIndex < flashcardList.size() - 1) {
                currentCardIndex++;
            } else if (retryIndices.isEmpty() && currentCardIndex == flashcardList.size() - 1) {
                showResults();
                return;
            }
            isAnswered = false;
            updateQuestionView();
        });

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
                Toast.makeText(QuestionModeActivity.this, "Lỗi tải câu hỏi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateQuestionView() {
        if (flashcardList.isEmpty()) return;
        Flashcard currentCard = flashcardList.get(currentCardIndex);
        textQuestion.setText(currentCard.question);
        buttonOptionA.setText("A. " + currentCard.option1);
        buttonOptionB.setText("B. " + currentCard.option2);
        buttonOptionC.setText("C. " + currentCard.option3);
        buttonOptionD.setText("D. " + currentCard.option4);
        textResult.setVisibility(View.GONE);
        enableOptions();
        isAnswered = false;
        buttonNext.setEnabled(false);
        buttonNext.setAlpha(0.3f);
    }

    private void checkAnswer(int selectedAnswer) {
        if (isAnswered || flashcardList.isEmpty()) return;
        Flashcard currentCard = flashcardList.get(currentCardIndex);
        int correctAnswer = currentCard.answer;
        String result = (selectedAnswer == correctAnswer) ? "Đúng!" : "Sai. Đáp án đúng: " + getCorrectOption(currentCard);
        textResult.setText(result);
        textResult.setVisibility(View.VISIBLE);
        disableOptions();
        isAnswered = true;
        buttonNext.setEnabled(true);
        buttonNext.setAlpha(1.0f);

        if (selectedAnswer != correctAnswer) {
            if (!retryIndices.contains(currentCardIndex)) {
                retryIndices.add(currentCardIndex);
            }
        } else if (!correctAnswers.contains(currentCardIndex)) {
            correctAnswers.add(currentCardIndex);
        }
    }

    private String getCorrectOption(Flashcard card) {
        String[] options = {card.option1, card.option2, card.option3, card.option4};
        return (card.answer >= 1 && card.answer <= 4) ? options[card.answer - 1] : "Không có đáp án hợp lệ.";
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

    private void showResults() {
        int totalQuestions = flashcardList.size();
        int correctCount = correctAnswers.size();
        double percentage = (correctCount * 100.0) / totalQuestions;
        String resultText = String.format("Kết quả: %d/%d đúng (%.1f%%)", correctCount, totalQuestions, percentage);
        textResult.setText(resultText);
        textResult.setVisibility(View.VISIBLE);
        disableOptions();
        buttonNext.setVisibility(View.GONE);

        // Show congrats only if all questions are correctly answered
        if (correctCount == totalQuestions) {
            showCongratsAnimation();
        }
    }

    private void showCongratsAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textResult, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textResult, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(textResult, "rotation", 0f, 360f);
        rotate.setDuration(1000);
        scaleX.setDuration(1000);
        scaleY.setDuration(1000);
        animatorSet.playTogether(scaleX, scaleY, rotate);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        textResult.setText("Chúc mừng bạn đã hoàn thành xuất sắc!");
        animatorSet.start();
    }
}