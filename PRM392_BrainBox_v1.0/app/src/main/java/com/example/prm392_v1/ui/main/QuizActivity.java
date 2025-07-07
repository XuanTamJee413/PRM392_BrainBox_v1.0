package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.BrainBoxDatabase;
import com.example.prm392_v1.data.entity.Quiz;
import com.example.prm392_v1.ui.adapters.QuizAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private BrainBoxDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupRecyclerView();

        database = BrainBoxDatabase.getInstance(this);

        loadQuizzes();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.quiz_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        quizAdapter = new QuizAdapter();
        recyclerView.setAdapter(quizAdapter);
    }

    private void loadQuizzes() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Quiz> quizList = database.quizDao().getAll();
            runOnUiThread(() -> {
                quizAdapter.submitList(quizList);
            });
        });
    }
}