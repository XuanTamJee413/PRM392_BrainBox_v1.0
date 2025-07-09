package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.prm392_v1.ui.adapters.FlashcardListAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizDetailActivity extends AppCompatActivity {

    private TextView textQuizTitle, textFlashcardContent, textCardCounter;
    private Button buttonPrev, buttonNext, buttonBack;

    private List<Flashcard> flashcardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private boolean isShowingQuestion = true;


    private RecyclerView termsRecyclerView;
    private FlashcardListAdapter flashcardListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_detail);

        int quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");


        textQuizTitle = findViewById(R.id.text_quiz_detail_title);
        textFlashcardContent = findViewById(R.id.text_flashcard_content);
        textCardCounter = findViewById(R.id.text_card_counter);
        buttonPrev = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);
        buttonBack = findViewById(R.id.button_back);
        termsRecyclerView = findViewById(R.id.terms_recycler_view);

        textQuizTitle.setText(quizName);

        setupTermsRecyclerView();
        setupClickListeners();

        if (quizId != -1) {
            fetchFlashcards(quizId);
        }
    }


    private void setupTermsRecyclerView() {
        termsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        flashcardListAdapter = new FlashcardListAdapter();
        termsRecyclerView.setAdapter(flashcardListAdapter);
    }

    private void fetchFlashcards(int quizId) {
        ApiService apiService = RetrofitClient.getApiService(this);
        String filter = "QuizId eq " + quizId;

        apiService.getFlashcardsForQuiz(filter).enqueue(new Callback<ODataResponse<Flashcard>>() {
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

        buttonBack.setOnClickListener(v -> {
            finish();
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