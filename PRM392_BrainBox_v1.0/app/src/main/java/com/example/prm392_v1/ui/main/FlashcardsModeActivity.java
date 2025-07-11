package com.example.prm392_v1.ui.main;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashcardsModeActivity extends AppCompatActivity {

    private TextView textQuizTitle, textFlashcardContent, textCardCounter;
    private Button buttonPrev, buttonNext, buttonBack;
    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isShowingQuestion = true;
    private int quizId;
    private View cardFlashcard;

    // For card flip animation
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false; // Tracks if the back of the card is visible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_mode);

        quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");

        initializeViews();
        textQuizTitle.setText(quizName);

        // Load animations
        loadAnimations();

        setupClickListeners();

        if (quizId != -1) {
            fetchFlashcards(quizId);
        }
    }

    private void initializeViews() {
        textQuizTitle = findViewById(R.id.text_quiz_title);
        textFlashcardContent = findViewById(R.id.text_flashcard_content);
        textCardCounter = findViewById(R.id.text_card_counter);
        buttonPrev = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);
        buttonBack = findViewById(R.id.button_back);
        cardFlashcard = findViewById(R.id.card_flashcard);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_right_out);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.card_flip_left_in);
    }

    private void setupClickListeners() {
        buttonNext.setOnClickListener(v -> {
            if (currentCardIndex < flashcardList.size() - 1) {
                // If currently showing answer, flip back to question before moving to next
                if (!isShowingQuestion) {
                    isShowingQuestion = true;
                    flipCardForNavigation(false); // Flip back to question
                } else {
                    currentCardIndex++;
                    updateCardView();
                }
            } else {
                // If already at the last card and next is pressed, and it's showing the question, flip to answer
                if (isShowingQuestion && !flashcardList.isEmpty()) {
                    flipCard(); // This will trigger the completion message if it's the last card
                } else {
                    Toast.makeText(this, "Bạn đã hoàn thành tất cả flashcard.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPrev.setOnClickListener(v -> {
            if (currentCardIndex > 0) {
                // If currently showing answer, flip back to question before moving to previous
                if (!isShowingQuestion) {
                    isShowingQuestion = true;
                    flipCardForNavigation(true); // Flip back to question
                } else {
                    currentCardIndex--;
                    updateCardView();
                }
            } else {
                Toast.makeText(this, "Bạn đang ở flashcard đầu tiên.", Toast.LENGTH_SHORT).show();
            }
        });

        cardFlashcard.setOnClickListener(v -> {
            if (!flashcardList.isEmpty() && flashcardList.size() > 0) {
                flipCard();
            }
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
                        updateCardView();
                        // Set camera distance for flip animation
                        float distance = 8000;
                        float scale = getResources().getDisplayMetrics().density * distance;
                        cardFlashcard.setCameraDistance(scale);
                    } else {
                        textFlashcardContent.setText("Không có flashcard nào.");
                        textCardCounter.setText("0 / 0");
                        buttonPrev.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(FlashcardsModeActivity.this, "Không tìm thấy flashcard hoặc lỗi phản hồi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(FlashcardsModeActivity.this, "Lỗi tải flashcard: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateCardView() {
        if (flashcardList.isEmpty()) return;

        Flashcard currentCard = flashcardList.get(currentCardIndex);
        if (isShowingQuestion) {
            textFlashcardContent.setText(currentCard.question);
        } else {
            String[] options = {currentCard.option1, currentCard.option2, currentCard.option3, currentCard.option4};
            String correctAnswer = (currentCard.answer >= 1 && currentCard.answer <= 4) ? options[currentCard.answer - 1] : "Không có đáp án hợp lệ.";
            textFlashcardContent.setText("Đáp án: " + correctAnswer);
        }
        textCardCounter.setText(String.format("%d / %d", currentCardIndex + 1, flashcardList.size()));

        // Enable/disable navigation buttons and adjust alpha
        buttonPrev.setEnabled(currentCardIndex > 0);
        buttonNext.setEnabled(currentCardIndex < flashcardList.size() - 1);
        buttonPrev.setAlpha(buttonPrev.isEnabled() ? 1.0f : 0.5f);
        buttonNext.setAlpha(buttonNext.isEnabled() ? 1.0f : 0.5f);
    }

    private void flipCard() {
        if (flashcardList.isEmpty()) return;

        // Disable interaction during animation
        cardFlashcard.setClickable(false);
        buttonPrev.setEnabled(false);
        buttonNext.setEnabled(false);
        buttonPrev.setAlpha(0.5f);
        buttonNext.setAlpha(0.5f);

        mSetRightOut.setTarget(cardFlashcard);
        mSetLeftIn.setTarget(cardFlashcard);

        mSetRightOut.start();
        mSetRightOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                isShowingQuestion = !isShowingQuestion;
                updateCardView(); // Update content after the first half of the flip
                mSetLeftIn.start();
                mSetLeftIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mIsBackVisible = !mIsBackVisible;
                        cardFlashcard.setClickable(true); // Re-enable interaction

                        // Re-enable navigation buttons based on current state
                        buttonPrev.setEnabled(currentCardIndex > 0);
                        buttonNext.setEnabled(currentCardIndex < flashcardList.size() - 1);
                        buttonPrev.setAlpha(buttonPrev.isEnabled() ? 1.0f : 0.5f);
                        buttonNext.setAlpha(buttonNext.isEnabled() ? 1.0f : 0.5f);

                        // Check if at the last card and showing the answer
                        if (currentCardIndex == flashcardList.size() - 1 && !isShowingQuestion) {
                            showCompletionMessage();
                        }
                    }

                    @Override public void onAnimationCancel(Animator animation) {}
                    @Override public void onAnimationRepeat(Animator animation) {}
                });
            }

            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
    }

    // This method is specifically for flipping back to the question when navigating
    private void flipCardForNavigation(boolean isMovingToPrevious) {
        // Only flip if currently showing the answer
        if (!isShowingQuestion) {
            // Disable interaction during animation
            cardFlashcard.setClickable(false);
            buttonPrev.setEnabled(false);
            buttonNext.setEnabled(false);
            buttonPrev.setAlpha(0.5f);
            buttonNext.setAlpha(0.5f);

            mSetRightOut.setTarget(cardFlashcard);
            mSetLeftIn.setTarget(cardFlashcard);

            mSetRightOut.start();
            mSetRightOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    isShowingQuestion = true; // Always flip back to question for navigation
                    updateCardView(); // Update content to question
                    mSetLeftIn.start();
                    mSetLeftIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mIsBackVisible = false; // Ensure front is visible
                            cardFlashcard.setClickable(true); // Re-enable interaction

                            // Now, after flipping, move to the next/previous card
                            if (isMovingToPrevious) {
                                currentCardIndex--;
                            } else {
                                currentCardIndex++;
                            }
                            updateCardView(); // Update with the new card's content
                        }
                        @Override public void onAnimationStart(Animator animation) {}
                        @Override public void onAnimationCancel(Animator animation) {}
                        @Override public void onAnimationRepeat(Animator animation) {}
                    });
                }
                @Override public void onAnimationStart(Animator animation) {}
                @Override public void onAnimationCancel(Animator animation) {}
                @Override public void onAnimationRepeat(Animator animation) {}
            });
        }
    }

    private void showCompletionMessage() {
        textFlashcardContent.setText("Chúc mừng bạn đã học xong!");
        textCardCounter.setText(String.format("%d / %d", flashcardList.size(), flashcardList.size())); // Show full count

        // Hide all interactive elements
        cardFlashcard.setClickable(false);
        buttonPrev.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.INVISIBLE); // Optionally hide back button too

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // After 2 seconds, finish this activity and return to the previous screen
            finish();
        }, 2000); // Display the completion message for 2 seconds
    }
}