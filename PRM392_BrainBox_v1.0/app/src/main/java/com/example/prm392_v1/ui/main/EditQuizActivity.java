package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.FlashcardUpdateDto;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.QuizUpdateDto;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.EditableFlashcardAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditQuizActivity extends AppCompatActivity {

    private TextInputEditText editQuizName, editQuizDescription;
    private Button buttonSaveChanges;
    private RecyclerView recyclerView;
    private EditableFlashcardAdapter adapter;

    private int quizId;
    private List<Flashcard> originalFlashcards = new ArrayList<>();
    private List<Flashcard> currentFlashcards = new ArrayList<>();
    private List<Integer> flashcardIdsToDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        if (quizId == -1) {
            Toast.makeText(this, "Lỗi: Không có ID của Quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        editQuizName = findViewById(R.id.edit_quiz_name);
        editQuizDescription = findViewById(R.id.edit_quiz_description);
        buttonSaveChanges = findViewById(R.id.button_save_changes);
        recyclerView = findViewById(R.id.recycler_editable_flashcards);

        setupRecyclerView();
        fetchQuizDetails();

        buttonSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void setupRecyclerView() {
        adapter = new EditableFlashcardAdapter(currentFlashcards, (flashcard, position) -> {
            if (flashcard.cardId > 0) {
                flashcardIdsToDelete.add(flashcard.cardId);
            }

            currentFlashcards.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, currentFlashcards.size());
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fetchQuizDetails() {
        ApiService apiService = RetrofitClient.getApiService(this);
        // Dùng $expand để lấy Quiz kèm theo danh sách Flashcard
        apiService.getQuizDetails(quizId, "Flashcards").enqueue(new Callback<Quiz>() {
            @Override
            public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Quiz quiz = response.body();
                    editQuizName.setText(quiz.quizName);
                    editQuizDescription.setText(quiz.description);

                    originalFlashcards.clear();
                    originalFlashcards.addAll(quiz.flashcards);
                    currentFlashcards.clear();
                    currentFlashcards.addAll(quiz.flashcards);

                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Quiz> call, Throwable t) {
                Toast.makeText(EditQuizActivity.this, "Lỗi tải chi tiết quiz", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChanges() {
        QuizUpdateDto updateDto = new QuizUpdateDto();
        updateDto.quizName = editQuizName.getText().toString();
        updateDto.description = editQuizDescription.getText().toString();
        updateDto.isPublic = true;


        List<FlashcardUpdateDto> flashcardsToUpdate = new ArrayList<>();
        for(Flashcard card : currentFlashcards) {
            FlashcardUpdateDto dto = new FlashcardUpdateDto();
            dto.cardId = card.cardId;
            dto.question = card.question;
            dto.option1 = card.option1;
            dto.option2 = card.option2;
            dto.option3 = card.option3;
            dto.option4 = card.option4;
            dto.answer = card.answer;
            flashcardsToUpdate.add(dto);
        }
        updateDto.flashcards = flashcardsToUpdate;
        updateDto.flashcardIdsToDelete = this.flashcardIdsToDelete;

        // Gọi API
        ApiService apiService = RetrofitClient.getApiService(this);
        apiService.updateQuiz(quizId, updateDto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(EditQuizActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditQuizActivity.this, "Cập nhật thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditQuizActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}