package com.example.prm392_v1.ui.main;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionModeActivity extends AppCompatActivity {

    private static final String TAG = "QuestionModeActivity";
    private TextView textQuizTitle, textQuestion, textResult;
    private Button buttonOptionA, buttonOptionB, buttonOptionC, buttonOptionD, buttonNext, buttonBack;

    private List<Flashcard> flashcardList = new ArrayList<>();
    // NEW: A single queue to manage which questions to ask.
    private List<Integer> questionsToAsk = new ArrayList<>();
    private int currentCardIndex = -1;
    private int totalQuestions = 0; // To calculate final percentage

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
        } else {
            Toast.makeText(this, "Không tìm thấy ID quiz.", Toast.LENGTH_SHORT).show();
            finish();
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

        // CHANGED: Next button's role is simplified to just load the next question.
        buttonNext.setOnClickListener(v -> loadNextQuestion());

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
                        // CHANGED: Initialize the quiz state here.
                        totalQuestions = flashcardList.size();
                        // Create a list of indices [0, 1, 2, ..., n-1]
                        questionsToAsk = IntStream.range(0, totalQuestions).boxed().collect(Collectors.toList());
                        loadNextQuestion(); // Load the first question
                        Log.d(TAG, "Fetched " + flashcardList.size() + " flashcards.");
                    } else {
                        textQuestion.setText("Không có câu hỏi nào.");
                        disableOptions();
                        buttonNext.setVisibility(View.GONE);
                        Log.d(TAG, "No flashcards found for quizId: " + quizId);
                    }
                } else {
                    Toast.makeText(QuestionModeActivity.this, "Không tìm thấy câu hỏi hoặc lỗi phản hồi.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to fetch flashcards. Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(QuestionModeActivity.this, "Lỗi tải câu hỏi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "API call for flashcards failed: " + t.getMessage(), t);
            }
        });
    }

    // NEW: Handles the logic for loading the next question from the queue.
    private void loadNextQuestion() {
        if (questionsToAsk.isEmpty()) {
            // No more questions to ask, all were answered correctly.
            showResults(true); // Show congrats
            return;
        }

        // Get the next question index from the front of the queue.
        currentCardIndex = questionsToAsk.remove(0);
        updateQuestionView();
    }

    private void updateQuestionView() {
        Flashcard currentCard = flashcardList.get(currentCardIndex);
        textQuestion.setText(currentCard.question);
        buttonOptionA.setText("A. " + currentCard.option1);
        buttonOptionB.setText("B. " + currentCard.option2);
        buttonOptionC.setText("C. " + currentCard.option3);
        buttonOptionD.setText("D. " + currentCard.option4);

        textResult.setVisibility(View.GONE);
        enableOptions();

        // The "Next" button should be disabled until an answer is provided.
        buttonNext.setEnabled(false);
        buttonNext.setAlpha(0.3f);

        Log.d(TAG, "Displaying question index: " + currentCardIndex + ". Questions remaining in queue: " + (questionsToAsk.size() + 1));
    }

    private void checkAnswer(int selectedAnswer) {
        // Disable options to prevent multiple answers
        disableOptions();
        // Enable the "Next" button so the user can proceed.
        buttonNext.setEnabled(true);
        buttonNext.setAlpha(1.0f);

        Flashcard currentCard = flashcardList.get(currentCardIndex);
        int correctAnswer = currentCard.answer;

        if (selectedAnswer == correctAnswer) {
            // Correct answer. The question is now considered "mastered" and is not added back to the queue.
            textResult.setText("Đúng!");
            textResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            Log.d(TAG, "Index " + currentCardIndex + " answered correctly.");
        } else {
            // Incorrect answer. Add the index to the back of the queue to be asked again later.
            questionsToAsk.add(currentCardIndex);
            String result = "Sai. Đáp án đúng: " + getCorrectOption(currentCard);
            textResult.setText(result);
            textResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            Log.d(TAG, "Index " + currentCardIndex + " answered incorrectly. Added back to queue. New queue size: " + questionsToAsk.size());
        }
        textResult.setVisibility(View.VISIBLE);
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

    // CHANGED: Renamed and simplified.
    private void showResults(boolean allCorrect) {
        textQuestion.setVisibility(View.GONE);
        disableOptions();
        buttonNext.setVisibility(View.GONE);

        if (allCorrect) {
            Log.d(TAG, "Showing congrats animation.");
            showCongratsAnimation();
        } else {
            // This case would be for quitting early, which isn't implemented here,
            // but the logic remains for completeness.
            String resultText = "Bạn đã hoàn thành!";
            textResult.setText(resultText);
            textResult.setVisibility(View.VISIBLE);
            Log.d(TAG, "Showing results without 100%.");
        }
    }

    private void showCongratsAnimation() {
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textResult, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textResult, "scaleY", 1f, 1.2f, 1f);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(textResult, "rotation", 0f, 360f);

        animatorSet.setDuration(1200);
        animatorSet.playTogether(scaleX, scaleY, rotate);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        textResult.setText("Chúc mừng bạn đã hoàn thành xuất sắc!");
        textResult.setVisibility(View.VISIBLE);
        animatorSet.start();
        Log.d(TAG, "Congrats animation started");

        // Finish activity after animation
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "Congrats animation ended, finishing activity");
                finish();
            }
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }
}