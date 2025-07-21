package com.example.prm392_v1.ui.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchModeActivity extends AppCompatActivity {

    private TextView textQuizTitle, textInstructions, textResult, textTimer;
    private GridLayout gridLayoutMatchCards;
    private Button buttonPlayAgain, buttonBack;

    private List<Flashcard> allAvailableFlashcards = new ArrayList<>();
    private List<Flashcard> unplayedFlashcards = new ArrayList<>();

    private List<MatchCard> currentMatchCards = new ArrayList<>();

    private View firstSelectedCardView = null;
    private int firstSelectedCardIndexInGrid = -1;
    private MatchCard firstSelectedMatchCard = null;

    private View secondSelectedCardView = null;
    private int secondSelectedCardIndexInGrid = -1;

    private int correctMatchesCount = 0;
    private int matchedPairsInCurrentRound = 0;
    private int currentRoundFlashcardCount = 4;

    private CountDownTimer gameTimer;
    private long timeLeftInMillis = 30000;
    private boolean timerRunning;
    private boolean isProcessingClick = false;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private static class MatchCard {
        int flashcardId;
        String content;
        boolean isQuestion;
        boolean isMatched;

        public MatchCard(int flashcardId, String content, boolean isQuestion) {
            this.flashcardId = flashcardId;
            this.content = content;
            this.isQuestion = isQuestion;
            this.isMatched = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_mode);

        int quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");

        initializeViews();
        textQuizTitle.setText(quizName);
        updateTimerText(); // Display initial timer value

        setupClickListeners();

        if (quizId != -1) {
            fetchFlashcards(quizId);
        } else {
            textInstructions.setText("Không có Quiz ID.");
            buttonPlayAgain.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);
    }

    private void initializeViews() {
        textQuizTitle = findViewById(R.id.text_quiz_title);
        textInstructions = findViewById(R.id.text_instructions);
        textResult = findViewById(R.id.text_result);
        textTimer = findViewById(R.id.text_timer);
        gridLayoutMatchCards = findViewById(R.id.grid_layout_match_cards);
        buttonPlayAgain = findViewById(R.id.button_play_again);
        buttonBack = findViewById(R.id.button_back);

        textResult.setText("Số cặp đúng: 0");
    }

    private void setupClickListeners() {
        buttonPlayAgain.setOnClickListener(v -> startGame());
        buttonBack.setOnClickListener(v -> finish());
    }

    private void fetchFlashcards(int quizId) {
        ApiService apiService = RetrofitClient.getApiService(this);
        String filter = "QuizId eq " + quizId;

        apiService.getFlashcardsByFilter(filter).enqueue(new Callback<ODataResponse<Flashcard>>() {
            @Override
            public void onResponse(Call<ODataResponse<Flashcard>> call, Response<ODataResponse<Flashcard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allAvailableFlashcards = response.body().value;
                    if (allAvailableFlashcards != null && !allAvailableFlashcards.isEmpty()) {
                        if (allAvailableFlashcards.size() < currentRoundFlashcardCount) {
                            Toast.makeText(MatchModeActivity.this, "Cần ít nhất " + currentRoundFlashcardCount + " flashcard để chơi chế độ ghép đôi (cho một lượt chơi).", Toast.LENGTH_LONG).show();
                            textInstructions.setText("Không đủ flashcard để chơi.");
                            buttonPlayAgain.setVisibility(View.GONE);
                            return;
                        }
                        startGame();
                    } else {
                        textInstructions.setText("Không có flashcard nào.");
                        buttonPlayAgain.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(MatchModeActivity.this, "Không thể tải flashcard. Lỗi phản hồi.", Toast.LENGTH_SHORT).show();
                    textInstructions.setText("Lỗi tải flashcard.");
                    buttonPlayAgain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(MatchModeActivity.this, "Lỗi tải flashcard: " + t.getMessage(), Toast.LENGTH_LONG).show();
                textInstructions.setText("Lỗi tải flashcard.");
                buttonPlayAgain.setVisibility(View.GONE);
            }
        });
    }

    private void startGame() {
        resetGameState();
        startNewRound();
        startTimer();
    }

    private void resetGameState() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        handler.removeCallbacksAndMessages(null);

        currentMatchCards.clear();
        correctMatchesCount = 0;
        matchedPairsInCurrentRound = 0;

        textResult.setText("Số cặp đúng: 0");
        resetSelectionState();
        timeLeftInMillis = 30000;
        updateTimerText();
        buttonPlayAgain.setVisibility(View.GONE);
        setGridClickable(true);
        isProcessingClick = false;

        unplayedFlashcards.clear();
        if (allAvailableFlashcards != null) {
            unplayedFlashcards.addAll(allAvailableFlashcards);
            Collections.shuffle(unplayedFlashcards);
        }
    }

    private void startNewRound() {
        gridLayoutMatchCards.removeAllViews();
        currentMatchCards.clear();
        matchedPairsInCurrentRound = 0;

        List<Flashcard> roundFlashcards = new ArrayList<>();

        for (int i = 0; i < currentRoundFlashcardCount && !unplayedFlashcards.isEmpty(); i++) {
            roundFlashcards.add(unplayedFlashcards.remove(0));
        }

        if (roundFlashcards.isEmpty()) {
            endGame();
            return;
        }

        List<MatchCard> allCardsInRound = new ArrayList<>();
        for (int i = 0; i < roundFlashcards.size(); i++) {
            Flashcard flashcard = roundFlashcards.get(i);
            allCardsInRound.add(new MatchCard(flashcard.cardId, flashcard.question, true));
            String correctAnswer = getCorrectOption(flashcard);
            allCardsInRound.add(new MatchCard(flashcard.cardId, correctAnswer, false));
        }

        Collections.shuffle(allCardsInRound, new Random());
        currentMatchCards.addAll(allCardsInRound);

        gridLayoutMatchCards.setColumnCount(2);
        gridLayoutMatchCards.setRowCount(currentMatchCards.size() / 2);

        int paddingPx = (int) (16 * getResources().getDisplayMetrics().density);
        int marginPx = (int) (4 * getResources().getDisplayMetrics().density);
        int gridTotalWidth = getResources().getDisplayMetrics().widthPixels - (2 * paddingPx);
        int desiredCardWidth = (gridTotalWidth / gridLayoutMatchCards.getColumnCount()) - (2 * marginPx);

        int numRows = Math.max(1, currentMatchCards.size() / gridLayoutMatchCards.getColumnCount());
        int gridTotalHeight = gridLayoutMatchCards.getHeight(); // Get current measured height of grid
        if (gridTotalHeight == 0) { // Fallback if height not yet measured (e.g. on first call)
            gridTotalHeight = (int) (getResources().getDisplayMetrics().heightPixels * 0.5); // Approx 50% screen height
        }
        int desiredCardHeight = (gridTotalHeight / numRows) - (2 * marginPx);
        if (desiredCardHeight <= 0) { // Prevent zero or negative height
            desiredCardHeight = (int) (100 * getResources().getDisplayMetrics().density); // Default minimum height
        }


        // Add views to grid
        for (int i = 0; i < currentMatchCards.size(); i++) {
            CardView cardView = (CardView) LayoutInflater.from(this).inflate(R.layout.item_match_card, gridLayoutMatchCards, false);
            TextView contentTextView = cardView.findViewById(R.id.text_match_content);
            contentTextView.setText(currentMatchCards.get(i).content);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = desiredCardWidth;
            params.height = desiredCardHeight;
            params.setMargins(marginPx, marginPx, marginPx, marginPx);
            params.rowSpec = GridLayout.spec(i / gridLayoutMatchCards.getColumnCount(), 1f);
            params.columnSpec = GridLayout.spec(i % gridLayoutMatchCards.getColumnCount(), 1f);

            cardView.setLayoutParams(params);
            cardView.setBackgroundColor(ContextCompat.getColor(this, R.color.cardview_light_background));

            final int cardIndexInGrid = i;
            cardView.setOnClickListener(v -> onCardClick(cardView, cardIndexInGrid));
            gridLayoutMatchCards.addView(cardView);
        }
        setGridClickable(true); // Ensure grid is clickable for new round
    }


    private void onCardClick(View cardView, int indexInGrid) {
        // Prevent clicks if timer isn't running, card is already matched,
        // it's the same card already selected, or another click is being processed
        if (!timerRunning || currentMatchCards.get(indexInGrid).isMatched ||
                cardView == firstSelectedCardView || isProcessingClick) {
            return;
        }

        isProcessingClick = true; // Set flag to prevent further clicks

        // Apply a visual feedback for selection
        cardView.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_200)); // Highlight selected
        ObjectAnimator animator = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 180f);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

        if (firstSelectedCardView == null) {
            // First card selected
            firstSelectedCardView = cardView;
            firstSelectedCardIndexInGrid = indexInGrid;
            firstSelectedMatchCard = currentMatchCards.get(indexInGrid);
            isProcessingClick = false; // Allow next click since only one card is selected
        } else {
            // Second card selected, store it
            secondSelectedCardView = cardView;
            secondSelectedCardIndexInGrid = indexInGrid;
            MatchCard secondSelectedMatchCard = currentMatchCards.get(indexInGrid);

            // Temporarily disable clicks on the whole grid until match check is done
            setGridClickable(false);

            if (firstSelectedMatchCard.flashcardId == secondSelectedMatchCard.flashcardId &&
                    firstSelectedMatchCard.isQuestion != secondSelectedMatchCard.isQuestion) {
                // It's a match!
                correctMatchesCount++;        // Total game score
                matchedPairsInCurrentRound++; // Score for current visible set
                textResult.setText("Số cặp đúng: " + correctMatchesCount);

                firstSelectedMatchCard.isMatched = true;
                secondSelectedMatchCard.isMatched = true;

                // Visually confirm match and make cards invisible after a short delay
                handler.postDelayed(() -> {
                    // Ensure views are still valid before trying to manipulate them
                    if (firstSelectedCardView != null && secondSelectedCardView != null) {
                        firstSelectedCardView.setVisibility(View.INVISIBLE);
                        firstSelectedCardView.setClickable(false); // Make invisible cards unclickable
                        firstSelectedCardView.setFocusable(false);

                        secondSelectedCardView.setVisibility(View.INVISIBLE);
                        secondSelectedCardView.setClickable(false); // Make invisible cards unclickable
                        secondSelectedCardView.setFocusable(false);
                    }
                    resetSelectionState(); // Clear selection states
                    isProcessingClick = false; // Allow clicks again
                    setGridClickable(true); // Re-enable clicks on other cards

                    checkRoundEnd(); // Check if all cards in the current round are matched
                }, 800); // Short delay to see the match before they disappear

            } else {
                // No match, flip cards back after a delay
                handler.postDelayed(this::resetSelectedCards, 1000); // Reset after 1 second
            }
        }
    }

    private void resetSelectedCards() {
        // Only flip back if the card hasn't been matched (e.g., if a rapid double-click resulted in a match)
        // And ensure the cardView itself is not null and is still visible
        if (firstSelectedCardView != null && firstSelectedCardIndexInGrid != -1 &&
                !currentMatchCards.get(firstSelectedCardIndexInGrid).isMatched &&
                firstSelectedCardView.getVisibility() == View.VISIBLE) {

            ObjectAnimator animator1 = ObjectAnimator.ofFloat(firstSelectedCardView, "rotationY", 180f, 0f);
            animator1.setDuration(300);
            animator1.setInterpolator(new AccelerateDecelerateInterpolator());
            animator1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (firstSelectedCardView != null && firstSelectedCardView.getVisibility() == View.VISIBLE) {
                        firstSelectedCardView.setBackgroundColor(ContextCompat.getColor(MatchModeActivity.this, R.color.cardview_light_background));
                    }
                }
            });
            animator1.start();
        }

        if (secondSelectedCardView != null && secondSelectedCardIndexInGrid != -1 &&
                !currentMatchCards.get(secondSelectedCardIndexInGrid).isMatched &&
                secondSelectedCardView.getVisibility() == View.VISIBLE) {

            ObjectAnimator animator2 = ObjectAnimator.ofFloat(secondSelectedCardView, "rotationY", 180f, 0f);
            animator2.setDuration(300);
            animator2.setInterpolator(new AccelerateDecelerateInterpolator());
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (secondSelectedCardView != null && secondSelectedCardView.getVisibility() == View.VISIBLE) {
                        secondSelectedCardView.setBackgroundColor(ContextCompat.getColor(MatchModeActivity.this, R.color.cardview_light_background));
                    }
                }
            });
            animator2.start();
        }

        resetSelectionState(); // Clear selection state variables
        isProcessingClick = false; // Allow clicks again after animations
        setGridClickable(true); // Re-enable clicks on the grid
    }

    private void resetSelectionState() {
        firstSelectedCardView = null;
        firstSelectedCardIndexInGrid = -1;
        firstSelectedMatchCard = null;
        secondSelectedCardView = null; // Clear second selected card as well
        secondSelectedCardIndexInGrid = -1;
    }

    private void setGridClickable(boolean clickable) {
        for (int i = 0; i < gridLayoutMatchCards.getChildCount(); i++) {
            View child = gridLayoutMatchCards.getChildAt(i);
            // Ensure child is not null
            if (child == null) continue;

            // Only set clickable/focusable if the MatchCard itself is not matched
            // and the view is visible.
            if (i < currentMatchCards.size() && currentMatchCards.get(i).isMatched) {
                child.setClickable(false);
                child.setFocusable(false);
            } else {
                child.setClickable(clickable);
                child.setFocusable(clickable);
                // Ensure non-matched, visible cards have default background if re-enabling clicks
                if (clickable && child.getVisibility() == View.VISIBLE) {
                    // Avoid resetting background if it's currently selected (teal_200)
                    // The onCardClick will handle the highlight, resetSelectedCards will revert it.
                    // This prevents a brief flash to default color if a card is currently selected.
                    if (child != firstSelectedCardView && child != secondSelectedCardView) {
                        child.setBackgroundColor(ContextCompat.getColor(this, R.color.cardview_light_background));
                    }
                }
            }
        }
    }

    private void startTimer() {
        gameTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                textTimer.setText("Thời gian: HẾT GIỜ!");
                endGame();
            }
        }.start();
        timerRunning = true;
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        textTimer.setText(String.format("Thời gian: %02ds", seconds));
    }

    private void checkRoundEnd() {
        // A round ends when all cards currently displayed are matched (invisible)
        // Or when matchedPairsInCurrentRound equals the number of pairs in this round
        if (matchedPairsInCurrentRound == currentMatchCards.size() / 2) {
            // All cards in the current view are matched
            if (unplayedFlashcards.isEmpty()) {
                // No more flashcards left in the entire pool, game truly ends
                endGame();
            } else {
                // There are still unplayed flashcards, start a new round
                Toast.makeText(this, "Hoàn thành vòng! Bắt đầu vòng mới...", Toast.LENGTH_SHORT).show();
                // A short delay before starting a new round for better user experience
                handler.postDelayed(this::startNewRound, 1200); // Delay for 1.2 seconds
            }
        }
    }

    private void endGame() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        timerRunning = false;
        textTimer.setText("HOÀN THÀNH!");
        setGridClickable(false); // Disable further interaction
        Toast.makeText(this, "Trò chơi kết thúc! Bạn đã tìm được " + correctMatchesCount + " cặp đúng.", Toast.LENGTH_LONG).show();
        buttonPlayAgain.setVisibility(View.VISIBLE); // Show play again button
    }

    private String getCorrectOption(Flashcard card) {
        String[] options = {card.option1, card.option2, card.option3, card.option4};
        // Ensure card.answer is valid, otherwise return a default or handle error
        if (card.answer >= 1 && card.answer <= options.length) {
            return options[card.answer - 1];
        }
        return "N/A"; // Or throw an exception/log an error
    }
}
