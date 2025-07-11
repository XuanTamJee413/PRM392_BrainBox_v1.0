package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.RatingQuiz;
import com.example.prm392_v1.data.model.RatingQuizRequest;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.FlashcardListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizDetailActivity extends AppCompatActivity {

    private TextView textQuizTitle, textFlashcardContent, textCardCounter;
    private Button buttonPrev, buttonNext, buttonBack, buttonSubmitRating;
    private RatingBar ratingBar;

    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isShowingQuestion = true;

    private RecyclerView termsRecyclerView;
    private FlashcardListAdapter flashcardListAdapter;
    private int quizId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");

        initializeViews();
        textQuizTitle.setText(quizName);

        setupTermsRecyclerView();
        setupClickListeners();

        if (quizId != -1) {
            fetchFlashcards(quizId);
            fetchMyRating(quizId);
        }
    }

    private void initializeViews() {
        textQuizTitle = findViewById(R.id.text_quiz_detail_title);
        textFlashcardContent = findViewById(R.id.text_flashcard_content);
        textCardCounter = findViewById(R.id.text_card_counter);
        buttonPrev = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);
        buttonBack = findViewById(R.id.button_back);
        termsRecyclerView = findViewById(R.id.terms_recycler_view);


        ratingBar = findViewById(R.id.rating_bar);
        buttonSubmitRating = findViewById(R.id.button_submit_rating);
    }

    private void setupClickListeners() {
        buttonNext.setOnClickListener(v -> {
            if (currentCardIndex < flashcardList.size() - 1) {
                currentCardIndex++;
                isShowingQuestion = true;
                updateCardView();
            }
        });

        buttonPrev.setOnClickListener(v -> {
            if (currentCardIndex > 0) {
                currentCardIndex--;
                isShowingQuestion = true;
                updateCardView();
            }
        });

        findViewById(R.id.card_flashcard).setOnClickListener(v -> {
            if (!flashcardList.isEmpty()) {
                isShowingQuestion = !isShowingQuestion;
                updateCardView();
            }
        });

        buttonBack.setOnClickListener(v -> finish());


        buttonSubmitRating.setOnClickListener(v -> submitRating());
    }

    private void setupTermsRecyclerView() {
        termsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flashcardListAdapter = new FlashcardListAdapter();
        termsRecyclerView.setAdapter(flashcardListAdapter);
    }

    private void fetchFlashcards(int quizId) {
        ApiService apiService = RetrofitClient.getApiService(this);
        String filter = "QuizId eq " + quizId;

        apiService.getFlashcardsByFilter(filter).enqueue(new Callback<ODataResponse<Flashcard>>() {
            @Override
            public void onResponse(Call<ODataResponse<Flashcard>> call, Response<ODataResponse<Flashcard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    flashcardList = response.body().value;
                    if (flashcardList != null && !flashcardList.isEmpty()) {
                        updateCardView();
                        flashcardListAdapter.submitList(flashcardList);
                    } else {
                        textFlashcardContent.setText("Không có flashcard nào.");
                        textCardCounter.setText("0 / 0");
                        buttonPrev.setVisibility(View.INVISIBLE);
                        buttonNext.setVisibility(View.INVISIBLE);
                    }
                }
            }
            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(QuizDetailActivity.this, "Lỗi tải flashcard", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void fetchMyRating(int quizId) {
        ApiService apiService = RetrofitClient.getApiService(this);

        apiService.getMyRatingForQuiz(quizId).enqueue(new Callback<RatingQuiz>() {
            @Override
            public void onResponse(Call<RatingQuiz> call, Response<RatingQuiz> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ratingBar.setRating(response.body().rating);
                }
            }
            @Override
            public void onFailure(Call<RatingQuiz> call, Throwable t) { }
        });
    }


    private void submitRating() {
        if (quizId == -1) return;

        int ratingValue = (int) ratingBar.getRating();
        if (ratingValue == 0) {
            Toast.makeText(this, "Vui lòng chọn ít nhất 1 sao", Toast.LENGTH_SHORT).show();
            return;
        }

        RatingQuizRequest request = new RatingQuizRequest(quizId, ratingValue, "");
        ApiService apiService = RetrofitClient.getApiService(this);

        apiService.submitRating(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuizDetailActivity.this, "Cảm ơn bạn đã đánh giá!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(QuizDetailActivity.this, "Gửi đánh giá thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(QuizDetailActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCardView() {
        if (flashcardList.isEmpty()) return;
        Flashcard currentCard = flashcardList.get(currentCardIndex);
        if (isShowingQuestion) {
            displayQuestion(currentCard);
        } else {
            displayAnswer(currentCard);
        }
        textCardCounter.setText(String.format("%d / %d", currentCardIndex + 1, flashcardList.size()));
        buttonPrev.setEnabled(currentCardIndex > 0);
        buttonNext.setEnabled(currentCardIndex < flashcardList.size() - 1);
        buttonPrev.setAlpha(buttonPrev.isEnabled() ? 1.0f : 0.3f);
        buttonNext.setAlpha(buttonNext.isEnabled() ? 1.0f : 0.3f);
    }

    private void displayQuestion(Flashcard card) {
        StringBuilder questionText = new StringBuilder();
        questionText.append("<b>").append(card.question).append("</b><br><br>");
        questionText.append("A. ").append(card.option1).append("<br>");
        questionText.append("B. ").append(card.option2).append("<br>");
        questionText.append("C. ").append(card.option3).append("<br>");
        questionText.append("D. ").append(card.option4);
        textFlashcardContent.setText(Html.fromHtml(questionText.toString(), Html.FROM_HTML_MODE_LEGACY));
    }

    private void displayAnswer(Flashcard card) {
        String[] options = {card.option1, card.option2, card.option3, card.option4};
        String correctAnswerText = "Không có đáp án hợp lệ.";
        if (card.answer >= 1 && card.answer <= 4) {
            correctAnswerText = options[card.answer - 1];
        }
        textFlashcardContent.setText("Đáp án đúng:\n" + correctAnswerText);
    }
}