package com.example.prm392_v1.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.prm392_v1.ui.main.CreateQuizActivity;
import com.example.prm392_v1.R;
import com.example.prm392_v1.data.entity.User;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.ManageQuizAdapter;
import com.example.prm392_v1.ui.main.EditQuizActivity;
import com.example.prm392_v1.utils.JwtUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment extends Fragment {

    private RecyclerView recyclerView;
    private ManageQuizAdapter adapter;
    private FloatingActionButton fabCreate;

    public QuizFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.manage_quiz_recycler_view);
        fabCreate = view.findViewById(R.id.fab_create_quiz);

        setupRecyclerView();
        setupListeners();

        fetchMyQuizzes();
    }

    private void setupRecyclerView() {
        adapter = new ManageQuizAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        fabCreate.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CreateQuizActivity.class);
            startActivity(intent);
        });

        adapter.setOnButtonClickListener(new ManageQuizAdapter.OnButtonClickListener() {
            @Override
            public void onEditClick(Quiz quiz) {
                Intent intent = new Intent(requireContext(), EditQuizActivity.class);
                intent.putExtra("EXTRA_QUIZ_ID", quiz.quizId);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Quiz quiz) {
                Toast.makeText(getContext(), "Xóa Quiz ID: " + quiz.quizId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMyQuizzes() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);
        if (token == null) {
            Toast.makeText(getContext(), "Lỗi xác thực", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = JwtUtils.parseUserFromToken(token);
        if (user == null) {
            Toast.makeText(getContext(), "Không thể lấy thông tin người dùng từ token", Toast.LENGTH_SHORT).show();
            return;
        }
        int userId = user.id;


        ApiService apiService = RetrofitClient.getApiService(requireContext());
        String filter = "CreatorId eq " + userId;

        apiService.getQuizzesByFilter(filter).enqueue(new Callback<ODataResponse<Quiz>>() {
            @Override
            public void onResponse(Call<ODataResponse<Quiz>> call, Response<ODataResponse<Quiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.submitList(response.body().value);
                } else {
                    Toast.makeText(getContext(), "Tải dữ liệu thất bại. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Quiz>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}