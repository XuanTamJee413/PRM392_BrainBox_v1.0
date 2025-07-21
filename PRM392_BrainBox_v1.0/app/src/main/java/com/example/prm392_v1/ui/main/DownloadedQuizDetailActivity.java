package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.BrainBoxDatabase;
import com.example.prm392_v1.data.entity.Flashcard;
import com.example.prm392_v1.ui.adapters.FlashcardListAdapter;

import java.util.ArrayList;
import java.util.List;

public class DownloadedQuizDetailActivity extends AppCompatActivity {
    private TextView textFlashcardContent, textCardCounter;
    private Button buttonPrev, buttonNext;
    private RecyclerView recyclerView;
    private FlashcardListAdapter adapter;
    Button buttonBack ;

    private List<com.example.prm392_v1.data.model.Flashcard> flashcardList = new ArrayList<>();
    private int currentIndex = 0;
    private boolean isShowingQuestion = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_downloaded_quiz_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textFlashcardContent = findViewById(R.id.text_flashcard_content);
        textCardCounter = findViewById(R.id.text_card_counter);
        buttonPrev = findViewById(R.id.button_previous);
        buttonNext = findViewById(R.id.button_next);
        buttonBack= findViewById(R.id.button_back);
        recyclerView = findViewById(R.id.terms_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlashcardListAdapter();
        recyclerView.setAdapter(adapter);

        int quizId = getIntent().getIntExtra("quiz_id", -1);
        if (quizId == -1) {
            Toast.makeText(this, "Quiz không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        TextView textTitle = findViewById(R.id.text_quiz_detail_title);
        String title = getIntent().getStringExtra("quiz_title");
        textTitle.setText(title != null ? title : "Quiz Id #" + quizId);

        buttonNext.setOnClickListener(v -> {
            if (currentIndex < flashcardList.size() - 1) {
                currentIndex++;
                isShowingQuestion = true;
                updateCardView();
            }
        });

        buttonPrev.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                isShowingQuestion = true;
                updateCardView();
            }
        });
        buttonBack.setOnClickListener(v -> finish());


        findViewById(R.id.card_flashcard).setOnClickListener(v -> {
            if (!flashcardList.isEmpty()) {
                isShowingQuestion = !isShowingQuestion;
                updateCardView();
            }
        });

        loadFlashcards(quizId);
    }
    private void loadFlashcards(int quizId) {
        new Thread(() -> {
            List<com.example.prm392_v1.data.entity.Flashcard> entities =
                    BrainBoxDatabase.getInstance(this).flashcardDao().getByQuizId(quizId);

            List<com.example.prm392_v1.data.model.Flashcard> models = new ArrayList<>();
            for (com.example.prm392_v1.data.entity.Flashcard e : entities) {
                com.example.prm392_v1.data.model.Flashcard m = new com.example.prm392_v1.data.model.Flashcard();
                m.cardId = e.cardId;
                m.quizId = e.quizId;
                m.question = e.question;
                m.option1 = e.option1;
                m.option2 = e.option2;
                m.option3 = e.option3;
                m.option4 = e.option4;
                m.answer = e.answer;
                models.add(m);
            }

            runOnUiThread(() -> {
                flashcardList = models;
                adapter.submitList(models);
                updateCardView();
            });
        }).start();
    }

    private void updateCardView() {
        if (flashcardList.isEmpty()) return;

        com.example.prm392_v1.data.model.Flashcard card = flashcardList.get(currentIndex);
        if (isShowingQuestion) {
            String content = "<b>" + card.question + "</b><br><br>"
                    + "A. " + card.option1 + "<br>"
                    + "B. " + card.option2 + "<br>"
                    + "C. " + card.option3 + "<br>"
                    + "D. " + card.option4;
            textFlashcardContent.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            String[] options = {card.option1, card.option2, card.option3, card.option4};
            String answer = (card.answer >= 1 && card.answer <= 4) ? options[card.answer - 1] : "Không rõ đáp án";
            textFlashcardContent.setText("Đáp án đúng:\n" + answer);
        }

        textCardCounter.setText((currentIndex + 1) + " / " + flashcardList.size());
        buttonPrev.setEnabled(currentIndex > 0);
        buttonNext.setEnabled(currentIndex < flashcardList.size() - 1);
    }
}