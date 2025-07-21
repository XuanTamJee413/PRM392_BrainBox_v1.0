package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.QuizCreateRequest;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.AddedFlashcardAdapter;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateQuizActivity extends AppCompatActivity {

    private TextInputEditText editQuizName, editQuizDescription;
    private TextInputEditText editQuestion, editOption1, editOption2, editOption3, editOption4;
    private AutoCompleteTextView autoCompleteCorrectAnswer;
    private Button buttonAddFlashcard, buttonSaveQuiz;

    private RecyclerView recyclerAddedFlashcards;
    private AddedFlashcardAdapter addedFlashcardAdapter;
    private List<Flashcard> newFlashcards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        editQuizName = findViewById(R.id.edit_quiz_name);
        editQuizDescription = findViewById(R.id.edit_quiz_description);
        editQuestion = findViewById(R.id.edit_flashcard_question);
        editOption1 = findViewById(R.id.edit_option1);
        editOption2 = findViewById(R.id.edit_option2);
        editOption3 = findViewById(R.id.edit_option3);
        editOption4 = findViewById(R.id.edit_option4);
        autoCompleteCorrectAnswer = findViewById(R.id.autoComplete_correct_answer);
        buttonAddFlashcard = findViewById(R.id.button_add_flashcard);
        buttonSaveQuiz = findViewById(R.id.button_save_quiz);
        recyclerAddedFlashcards = findViewById(R.id.recycler_added_flashcards);

        setupRecyclerView();
        setupAnswerDropdown();

        // Cài đặt sự kiện
        buttonAddFlashcard.setOnClickListener(v -> addFlashcardToList());
        buttonSaveQuiz.setOnClickListener(v -> saveQuizToApi());
    }


    private void setupRecyclerView() {
        addedFlashcardAdapter = new AddedFlashcardAdapter(newFlashcards);
        recyclerAddedFlashcards.setLayoutManager(new LinearLayoutManager(this));
        recyclerAddedFlashcards.setAdapter(addedFlashcardAdapter);
    }

    private void setupAnswerDropdown() {
        String[] answerOptions = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, answerOptions);
        autoCompleteCorrectAnswer.setAdapter(adapter);
    }

    private void addFlashcardToList() {
        String question = editQuestion.getText().toString().trim();
        String op1 = editOption1.getText().toString().trim();
        String op2 = editOption2.getText().toString().trim();
        String op3 = editOption3.getText().toString().trim();
        String op4 = editOption4.getText().toString().trim();
        String answerStr = autoCompleteCorrectAnswer.getText().toString().trim();

        if (question.isEmpty() || op1.isEmpty() || op2.isEmpty() || op3.isEmpty() || op4.isEmpty() || answerStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin cho flashcard", Toast.LENGTH_SHORT).show();
            return;
        }

        Flashcard flashcard = new Flashcard();
        flashcard.question = question;
        flashcard.option1 = op1;
        flashcard.option2 = op2;
        flashcard.option3 = op3;
        flashcard.option4 = op4;
        flashcard.answer = Integer.parseInt(answerStr);

        newFlashcards.add(flashcard);
        addedFlashcardAdapter.notifyItemInserted(newFlashcards.size() - 1);


        editQuestion.setText("");
        editOption1.setText("");
        editOption2.setText("");
        editOption3.setText("");
        editOption4.setText("");
        autoCompleteCorrectAnswer.setText("", false);
        editQuestion.requestFocus();

        Toast.makeText(this, "Đã thêm thẻ! Tổng số thẻ: " + newFlashcards.size(), Toast.LENGTH_SHORT).show();
    }

    private void saveQuizToApi() {
        String quizName = editQuizName.getText().toString().trim();
        String description = editQuizDescription.getText().toString().trim();

        if (quizName.isEmpty() || newFlashcards.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên Quiz và thêm ít nhất 1 thẻ", Toast.LENGTH_SHORT).show();
            return;
        }

        QuizCreateRequest request = new QuizCreateRequest(quizName, description, true, newFlashcards);
        ApiService apiService = RetrofitClient.getApiService(this);

        apiService.createQuizWithFlashcards(request).enqueue(new Callback<Quiz>() {
            @Override
            public void onResponse(Call<Quiz> call, Response<Quiz> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateQuizActivity.this, "Tạo Quiz thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(CreateQuizActivity.this, "Lỗi khi tạo quiz: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Quiz> call, Throwable t) {
                Toast.makeText(CreateQuizActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}