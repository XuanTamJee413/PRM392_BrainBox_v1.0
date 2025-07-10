package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.FlashcardUpdateDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.QuizUpdateDto;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.EditableFlashcardAdapter;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditQuizActivity extends AppCompatActivity {

    private TextInputEditText editQuizName, editQuizDescription;
    private Button buttonSaveChanges;
    private RecyclerView recyclerView;
    private EditableFlashcardAdapter adapter;
    private TextView textAddEditTitle;
    private TextInputEditText editQuestion, editOption1, editOption2, editOption3, editOption4;
    private AutoCompleteTextView autoCompleteCorrectAnswer;
    private Button buttonAddOrUpdateFlashcard;

    private int quizId;
    private List<Flashcard> currentFlashcards = new ArrayList<>();
    private List<Integer> flashcardIdsToDelete = new ArrayList<>();
    private Flashcard flashcardBeingEdited = null;
    private int editingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        quizId = getIntent().getIntExtra("EXTRA_QUIZ_ID", -1);
        String quizName = getIntent().getStringExtra("EXTRA_QUIZ_NAME");
        String quizDescription = getIntent().getStringExtra("EXTRA_QUIZ_DESCRIPTION");

        if (quizId == -1) {
            Toast.makeText(this, "Lỗi: Không có ID của Quiz", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initializeViews();
        editQuizName.setText(quizName);
        editQuizDescription.setText(quizDescription);

        setupRecyclerView();
        setupAnswerDropdown();
        setupClickListeners();

        fetchFlashcards(quizId);
    }

    private void initializeViews() {
        editQuizName = findViewById(R.id.edit_quiz_name);
        editQuizDescription = findViewById(R.id.edit_quiz_description);
        buttonSaveChanges = findViewById(R.id.button_save_changes);
        recyclerView = findViewById(R.id.recycler_editable_flashcards);
        textAddEditTitle = findViewById(R.id.text_add_edit_title);
        editQuestion = findViewById(R.id.edit_flashcard_question);
        editOption1 = findViewById(R.id.edit_option1);
        editOption2 = findViewById(R.id.edit_option2);
        editOption3 = findViewById(R.id.edit_option3);
        editOption4 = findViewById(R.id.edit_option4);
        autoCompleteCorrectAnswer = findViewById(R.id.autoComplete_correct_answer);
        buttonAddOrUpdateFlashcard = findViewById(R.id.button_add_or_update_flashcard);
    }

    private void setupRecyclerView() {
        adapter = new EditableFlashcardAdapter(currentFlashcards);
        adapter.setOnButtonClickListener(new EditableFlashcardAdapter.OnButtonClickListener() {
            @Override
            public void onDeleteClick(int position) {
                Flashcard cardToDelete = currentFlashcards.get(position);
                if (cardToDelete.cardId > 0) {
                    flashcardIdsToDelete.add(cardToDelete.cardId);
                }
                currentFlashcards.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, currentFlashcards.size());
            }

            @Override
            public void onEditClick(int position) {
                populateFormForEditing(currentFlashcards.get(position), position);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fetchFlashcards(int quizId) {
        ApiService apiService = RetrofitClient.getApiService(this);
        String filter = "QuizId eq " + quizId;
        apiService.getFlashcardsByFilter(filter).enqueue(new Callback<ODataResponse<Flashcard>>() {
            @Override
            public void onResponse(Call<ODataResponse<Flashcard>> call, Response<ODataResponse<Flashcard>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentFlashcards.clear();
                    if(response.body().value != null) {
                        currentFlashcards.addAll(response.body().value);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ODataResponse<Flashcard>> call, Throwable t) {
                Toast.makeText(EditQuizActivity.this, "Lỗi tải danh sách Flashcard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        buttonSaveChanges.setOnClickListener(v -> saveChanges());
        buttonAddOrUpdateFlashcard.setOnClickListener(v -> handleAddOrUpdateCard());
    }

    private void populateFormForEditing(Flashcard flashcard, int position) {
        flashcardBeingEdited = flashcard;
        editingPosition = position;

        textAddEditTitle.setText("Sửa Flashcard");
        editQuestion.setText(flashcard.question);
        editOption1.setText(flashcard.option1);
        editOption2.setText(flashcard.option2);
        editOption3.setText(flashcard.option3);
        editOption4.setText(flashcard.option4);
        autoCompleteCorrectAnswer.setText(String.valueOf(flashcard.answer), false);
        buttonAddOrUpdateFlashcard.setText("Cập nhật thẻ");
        editQuestion.requestFocus();
    }

    private void handleAddOrUpdateCard() {
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

        if (flashcardBeingEdited != null) { // Chế độ Sửa
            flashcardBeingEdited.question = question;
            flashcardBeingEdited.option1 = op1;
            flashcardBeingEdited.option2 = op2;
            flashcardBeingEdited.option3 = op3;
            flashcardBeingEdited.option4 = op4;
            flashcardBeingEdited.answer = Integer.parseInt(answerStr);
            adapter.notifyItemChanged(editingPosition);
            Toast.makeText(this, "Đã cập nhật thẻ!", Toast.LENGTH_SHORT).show();
        } else { // Chế độ Thêm mới
            Flashcard newCard = new Flashcard();
            newCard.cardId = 0;
            newCard.question = question;
            newCard.option1 = op1;
            newCard.option2 = op2;
            newCard.option3 = op3;
            newCard.option4 = op4;
            newCard.answer = Integer.parseInt(answerStr);
            currentFlashcards.add(newCard);
            adapter.notifyItemInserted(currentFlashcards.size() - 1);
            Toast.makeText(this, "Đã thêm thẻ mới!", Toast.LENGTH_SHORT).show();
        }
        resetFlashcardForm();
    }

    private void resetFlashcardForm() {
        flashcardBeingEdited = null;
        editingPosition = -1;
        textAddEditTitle.setText("Thêm Flashcard mới");
        editQuestion.setText("");
        editOption1.setText("");
        editOption2.setText("");
        editOption3.setText("");
        editOption4.setText("");
        autoCompleteCorrectAnswer.setText("", false);
        buttonAddOrUpdateFlashcard.setText("Thêm thẻ");
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

    private void setupAnswerDropdown() {
        String[] answerOptions = new String[]{"1", "2", "3", "4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, answerOptions);
        autoCompleteCorrectAnswer.setAdapter(adapter);
    }
}