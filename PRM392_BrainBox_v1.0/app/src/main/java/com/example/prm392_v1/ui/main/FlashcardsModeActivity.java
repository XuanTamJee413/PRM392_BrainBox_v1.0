package com.example.prm392_v1.ui.main;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;
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

    private static final String TAG = "FlashcardsModeActivity";
    private TextView textQuizTitle, textFlashcardContent, textCardCounter;
    private Button buttonPrev, buttonNext, buttonBack;
    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isShowingQuestion = true;
    private int quizId;
    private View cardFlashcard;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean isAnimating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcards_mode);

        quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");

        initializeViews();
        textQuizTitle.setText(quizName);

        loadAnimations();

        float distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        cardFlashcard.setCameraDistance(scale);

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
        mSetRightOut.setDuration(300);
        mSetLeftIn.setDuration(300);
    }

    private void setupClickListeners() {
        buttonNext.setOnClickListener(v -> {
            if (flashcardList.isEmpty()) {
                Toast.makeText(this, "Không có flashcard nào.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentCardIndex < flashcardList.size() - 1) {
                // Move to next card
                if (!isShowingQuestion) {
                    isShowingQuestion = true;
                }
                currentCardIndex++;
                updateCardView();
                Log.d(TAG, "Moved to next card: index=" + currentCardIndex);
            } else if (isShowingQuestion) {
                // On last card, flip to answer
                flipCard();
            } else {
                // On last card, showing answer: trigger completion
                showCompletionMessage();
            }
        });

        buttonPrev.setOnClickListener(v -> {
            if (flashcardList.isEmpty()) {
                Toast.makeText(this, "Không có flashcard nào.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentCardIndex > 0) {
                // Move to previous card
                if (!isShowingQuestion) {
                    isShowingQuestion = true;
                }
                currentCardIndex--;
                updateCardView();
                Log.d(TAG, "Moved to previous card: index=" + currentCardIndex);
            } else {
                Toast.makeText(this, "Bạn đang ở flashcard đầu tiên.", Toast.LENGTH_SHORT).show();
            }
        });

        cardFlashcard.setOnClickListener(v -> {
            if (!flashcardList.isEmpty() && !isAnimating) {
                flipCard();
            } else if (flashcardList.isEmpty()) {
                Toast.makeText(this, "Không có flashcard nào để lật.", Toast.LENGTH_SHORT).show();
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
                        Log.d(TAG, "Fetched " + flashcardList.size() + " flashcards for quizId: " + quizId);
                    } else {
                        textFlashcardContent.setText("Không có flashcard nào.");
                        textCardCounter.setText("0 / 0");
                        buttonPrev.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "No flashcards found for quizId: " + quizId);
                    }
                } else {
                    Toast.makeText(FlashcardsModeActivity.this, "Không tìm thấy flashcard hoặc lỗi phản hồi.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to fetch flashcards. Code: " + response.code() + ", Message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(FlashcardsModeActivity.this, "Lỗi tải flashcard: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e(TAG, "API call for flashcards failed: " + t.getMessage(), t);
            }
        });
    }

    private void updateCardView() {
        if (flashcardList.isEmpty()) {
            textFlashcardContent.setText("Không có flashcard nào.");
            textCardCounter.setText("0 / 0");
            buttonPrev.setEnabled(false);
            buttonNext.setEnabled(false);
            buttonPrev.setAlpha(0.5f);
            buttonNext.setAlpha(0.5f);
            return;
        }

        Flashcard currentCard = flashcardList.get(currentCardIndex);
        if (isShowingQuestion) {
            textFlashcardContent.setText(currentCard.question);
        } else {
            String[] options = {currentCard.option1, currentCard.option2, currentCard.option3, currentCard.option4};
            String correctAnswer = (currentCard.answer >= 1 && currentCard.answer <= 4) ? options[currentCard.answer - 1] : "Không có đáp án hợp lệ.";
            textFlashcardContent.setText("Đáp án: " + correctAnswer);
        }
        textCardCounter.setText(String.format("%d / %d", currentCardIndex + 1, flashcardList.size()));

        buttonPrev.setEnabled(currentCardIndex > 0);
        buttonNext.setEnabled(currentCardIndex < flashcardList.size() - 1 || isShowingQuestion);
        buttonPrev.setAlpha(buttonPrev.isEnabled() ? 1.0f : 0.5f);
        buttonNext.setAlpha(buttonNext.isEnabled() ? 1.0f : 0.5f);
        Log.d(TAG, "Updated card view: index=" + currentCardIndex + ", isShowingQuestion=" + isShowingQuestion);
    }

    private void flipCard() {
        if (flashcardList.isEmpty() || isAnimating) {
            Log.w(TAG, "Cannot flip: empty list or animation in progress");
            return;
        }

        isAnimating = true;
        cardFlashcard.setClickable(false);
        buttonPrev.setEnabled(false);
        buttonNext.setEnabled(false);
        buttonPrev.setAlpha(0.5f);
        buttonNext.setAlpha(0.5f);

        mSetRightOut.setTarget(cardFlashcard);
        mSetLeftIn.setTarget(cardFlashcard);

        isShowingQuestion = !isShowingQuestion;
        updateCardView();

        mSetRightOut.start();
        mSetRightOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.d(TAG, "Flip animation started: right out");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mSetLeftIn.start();
                mSetLeftIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        Log.d(TAG, "Flip animation started: left in");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isAnimating = false; // Reset animation flag
                        cardFlashcard.setClickable(true); // Re-enable interaction
                        buttonPrev.setEnabled(currentCardIndex > 0);
                        buttonNext.setEnabled(currentCardIndex < flashcardList.size() - 1 || isShowingQuestion);
                        buttonPrev.setAlpha(buttonPrev.isEnabled() ? 1.0f : 0.5f);
                        buttonNext.setAlpha(buttonNext.isEnabled() ? 1.0f : 0.5f);
                        Log.d(TAG, "Flip animation completed: isShowingQuestion=" + isShowingQuestion);

                        if (currentCardIndex == flashcardList.size() - 1 && !isShowingQuestion) {
                            showCompletionMessage();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        isAnimating = false;
                        cardFlashcard.setClickable(true);
                        Log.w(TAG, "Flip animation cancelled: left in");
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isAnimating = false;
                cardFlashcard.setClickable(true);
                Log.w(TAG, "Flip animation cancelled: right out");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    private void showCompletionMessage() {
        textFlashcardContent.setText("Chúc mừng bạn đã học xong!");
        textCardCounter.setText(String.format("%d / %d", flashcardList.size(), flashcardList.size()));
        cardFlashcard.setClickable(false);
        buttonPrev.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonBack.setVisibility(View.INVISIBLE);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Log.d(TAG, "Completion message displayed, finishing activity");
            finish();
        }, 2000);
    }
}