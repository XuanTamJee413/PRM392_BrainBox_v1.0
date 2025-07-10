package com.example.prm392_v1.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.QuizDto;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.DocumentAdapter;
import com.example.prm392_v1.ui.adapters.QuizAdapter;
import com.example.prm392_v1.ui.auth.LoginActivity;
import com.example.prm392_v1.ui.main.DocumentDetailActivity;
import com.example.prm392_v1.ui.main.PurchaseActivity;
import com.example.prm392_v1.ui.main.QuizActivity;
import com.example.prm392_v1.ui.main.QuizDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerDocuments;
    private RecyclerView recyclerQuizzes;
    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        View.OnClickListener upgradeClickListener = v -> {
            String token = requireContext()
                    .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    .getString("jwt_token", null);

            if (token == null || token.isEmpty()) {
                Toast.makeText(requireContext(), "Bạn phải đăng nhập trước", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }
            int id = v.getId();
            String selectedPackage = "";

            if (id == R.id.btn_upgrade_lifetime) {
                selectedPackage = "lifetime";
            } else if (id == R.id.btn_upgrade_30days) {
                selectedPackage = "30days";
            } else if (id == R.id.btn_upgrade_6months) {
                selectedPackage = "6months";
            } else if (id == R.id.btn_upgrade_12months) {
                selectedPackage = "12months";
            }

            Intent intent = new Intent(requireContext(), PurchaseActivity.class);
            intent.putExtra("selected_package", selectedPackage);
            startActivity(intent);
        };
        view.findViewById(R.id.btn_upgrade_lifetime).setOnClickListener(upgradeClickListener);
        view.findViewById(R.id.btn_upgrade_30days).setOnClickListener(upgradeClickListener);
        view.findViewById(R.id.btn_upgrade_6months).setOnClickListener(upgradeClickListener);
        view.findViewById(R.id.btn_upgrade_12months).setOnClickListener(upgradeClickListener);


        view.findViewById(R.id.text_see_all_docs).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), QuizActivity.class)); // fall back tam quiz =))))))
        });

        view.findViewById(R.id.text_see_all_quizzes).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), QuizActivity.class));
        });

        recyclerDocuments = view.findViewById(R.id.recycler_top_documents);
        recyclerQuizzes = view.findViewById(R.id.recycler_latest_quizzes);

        recyclerDocuments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerQuizzes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        ApiService apiService = RetrofitClient.getApiService(requireContext());

        apiService.getTopDocuments(null, "Views desc", 5,"Author($select=Username)"
        ).enqueue(new Callback<ODataResponse<DocumentDto>>() {
            @Override
            public void onResponse(Call<ODataResponse<DocumentDto>> call, Response<ODataResponse<DocumentDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DocumentDto> docs = response.body().value;
                    if (docs == null || docs.isEmpty()) {
                        recyclerDocuments.setVisibility(View.GONE);
                        view.findViewById(R.id.text_no_documents).setVisibility(View.VISIBLE);
                    } else {
                        recyclerDocuments.setVisibility(View.VISIBLE);
                        view.findViewById(R.id.text_no_documents).setVisibility(View.GONE);
                        DocumentAdapter documentAdapter = new DocumentAdapter();
                        recyclerDocuments.setAdapter(documentAdapter);
                        documentAdapter.submitList(docs);
                        documentAdapter.setOnItemClickListener(document -> {
                            Intent intent = new Intent(requireContext(), DocumentDetailActivity.class);
                            intent.putExtra("EXTRA_DOC_ID", document.DocId);
                            intent.putExtra("EXTRA_DOC_TITLE", document.Title);
                            startActivity(intent);
                        });

                    }
                } else {
                    recyclerDocuments.setVisibility(View.GONE);
                    view.findViewById(R.id.text_no_documents).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.text_no_documents)).setText("Không tải được dữ liệu.");
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<DocumentDto>> call, Throwable t) {
                recyclerDocuments.setVisibility(View.GONE);
                view.findViewById(R.id.text_no_documents).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.text_no_documents)).setText("Lỗi kết nối.");
            }
        });

        apiService.getLatestQuizzes(
                null,
                "CreatedAt desc",
                5,
                "Creator($select=Username)"
        ).enqueue(new Callback<ODataResponse<QuizDto>>() {
            @Override
            public void onResponse(Call<ODataResponse<QuizDto>> call, Response<ODataResponse<QuizDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuizDto> quizzes = response.body().value;
                    if (quizzes == null || quizzes.isEmpty()) {
                        recyclerQuizzes.setVisibility(View.GONE);
                        view.findViewById(R.id.text_no_quizzes).setVisibility(View.VISIBLE);
                    } else {
                        recyclerQuizzes.setVisibility(View.VISIBLE);
                        view.findViewById(R.id.text_no_quizzes).setVisibility(View.GONE);
                        List<Quiz> quizList = new ArrayList<>();
                        for (QuizDto dto : quizzes) {
                            Quiz quiz = new Quiz();
                            quiz.quizId = dto.QuizId;
                            quiz.quizName = dto.QuizName;
                            quiz.description = dto.Description;
                            quizList.add(quiz);
                        }

                        QuizAdapter quizAdapter = new QuizAdapter();
                        recyclerQuizzes.setAdapter(quizAdapter);
                        quizAdapter.submitList(quizList);
                        quizAdapter.setOnItemClickListener(quiz -> {
                            Intent intent = new Intent(requireContext(), QuizDetailActivity.class);
                            intent.putExtra("EXTRA_QUIZ_ID", quiz.quizId);
                            intent.putExtra("EXTRA_QUIZ_NAME", quiz.quizName);
                            startActivity(intent);
                        });

                    }
                } else {
                    recyclerQuizzes.setVisibility(View.GONE);
                    view.findViewById(R.id.text_no_quizzes).setVisibility(View.VISIBLE);
                    ((TextView) view.findViewById(R.id.text_no_quizzes)).setText("Không tải được dữ liệu.");
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<QuizDto>> call, Throwable t) {
                recyclerQuizzes.setVisibility(View.GONE);
                view.findViewById(R.id.text_no_quizzes).setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.text_no_quizzes)).setText("Lỗi kết nối.");
            }
        });


        return view;
    }
}