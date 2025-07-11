package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.QuizAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizActivity extends AppCompatActivity implements QuizAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private QuizAdapter quizAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private List<Quiz> fullQuizList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        progressBar = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        Button btnBackHome = findViewById(R.id.btn_back_home);

        setupRecyclerView();
        setupSearchView();

        btnBackHome.setOnClickListener(v -> finish());

        loadQuizzesFromApi();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.quiz_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        quizAdapter = new QuizAdapter();
        recyclerView.setAdapter(quizAdapter);
        quizAdapter.setOnItemClickListener(this); // Gán listener
    }

    @Override
    public void onItemClick(Quiz quiz) {
        Intent intent = new Intent(this, QuizDetailActivity.class);
        intent.putExtra("EXTRA_QUIZ_ID", quiz.quizId);
        intent.putExtra("EXTRA_QUIZ_NAME", quiz.quizName);
        startActivity(intent);
    }

    private void loadQuizzesFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getApiService(this);

        apiService.getAllQuizzes().enqueue(new Callback<ODataResponse<Quiz>>() {
            @Override
            public void onResponse(Call<ODataResponse<Quiz>> call, Response<ODataResponse<Quiz>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    fullQuizList = response.body().value;
                    quizAdapter.submitList(fullQuizList);
                } else {
                    Toast.makeText(QuizActivity.this, "Không thể tải dữ liệu. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Quiz>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(QuizActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Disable submit action
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText); // Call filter method as user types
                return true; // Indicate the event is handled
            }
        });

        // Clear initial focus to prevent keyboard from opening automatically
        searchView.clearFocus();
    }

    private void filter(String text) {
        List<Quiz> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(fullQuizList);
        } else {
            for (Quiz item : fullQuizList) {
                if (item.quizName.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        quizAdapter.submitList(filteredList);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (searchView.hasFocus()) {
                int[] location = new int[2];
                searchView.getLocationOnScreen(location);
                int left = location[0];
                int top = location[1];
                int right = left + searchView.getWidth();
                int bottom = top + searchView.getHeight();

                float x = event.getRawX();
                float y = event.getRawY();
                if (x < left || x > right || y < top || y > bottom) {
                    searchView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}