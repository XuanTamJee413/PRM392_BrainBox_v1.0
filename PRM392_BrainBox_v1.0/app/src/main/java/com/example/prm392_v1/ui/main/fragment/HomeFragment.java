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
import com.example.prm392_v1.ui.main.ViewDocumentActivity;
import com.example.prm392_v1.utils.DocumentDownloader;
import com.example.prm392_v1.utils.QuizDownloader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerDocuments, recyclerQuizzes;
    private View rootView;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setupUpgradeButtons();
        setupSeeAllButtons();
        setupRecyclerViews();
        fetchTopDocuments();
        fetchLatestQuizzes();

        return rootView;
    }

    private void setupUpgradeButtons() {
        int[] buttonIds = {
                R.id.btn_upgrade_lifetime,
                R.id.btn_upgrade_30days,
                R.id.btn_upgrade_6months,
                R.id.btn_upgrade_12months
        };

        View.OnClickListener listener = v -> {
            String token = requireContext()
                    .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    .getString("jwt_token", null);

            if (token == null || token.isEmpty()) {
                Toast.makeText(requireContext(), "Bạn phải đăng nhập trước", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), LoginActivity.class));
                return;
            }

            String selectedPackage = "";

            int id = v.getId();
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

        for (int id : buttonIds) {
            rootView.findViewById(id).setOnClickListener(listener);
        }
    }

    private void setupSeeAllButtons() {
        rootView.findViewById(R.id.text_see_all_docs).setOnClickListener(v -> {
            ViewDocumentActivity.start(requireContext());
        });
        rootView.findViewById(R.id.text_see_all_quizzes).setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), QuizActivity.class));
        });
    }

    private void setupRecyclerViews() {
        recyclerDocuments = rootView.findViewById(R.id.recycler_top_documents);
        recyclerQuizzes = rootView.findViewById(R.id.recycler_latest_quizzes);

        recyclerDocuments.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerQuizzes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void fetchTopDocuments() {
        ApiService apiService = RetrofitClient.getApiService(requireContext());
        apiService.getTopDocuments(null, "Views desc", 5, "Author($select=Username)")
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ODataResponse<DocumentDto>> call, Response<ODataResponse<DocumentDto>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<DocumentDto> docs = response.body().value;
                            showOrHideRecycler(recyclerDocuments, R.id.text_no_documents, docs);

                            if (docs != null && !docs.isEmpty()) {
                                DocumentAdapter adapter = new DocumentAdapter();
                                adapter.submitList(docs);
                                recyclerDocuments.setAdapter(adapter);
                                adapter.setOnItemClickListener(document -> {
                                    Intent intent = new Intent(requireContext(), DocumentDetailActivity.class);
                                    intent.putExtra("EXTRA_DOC_ID", document.DocId);
                                    intent.putExtra("EXTRA_DOC_TITLE", document.Title);
                                    startActivity(intent);
                                });
                                adapter.setOnDownloadClickListener(document -> {
                                    DocumentDownloader.downloadDocumentWithDetails(requireContext(), document);
                                });


                            }
                        } else {
                            showErrorText(R.id.text_no_documents, "Không tải được dữ liệu.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ODataResponse<DocumentDto>> call, Throwable t) {
                        showErrorText(R.id.text_no_documents, "Lỗi kết nối.");
                    }
                });
    }

    private void fetchLatestQuizzes() {
        ApiService apiService = RetrofitClient.getApiService(requireContext());
        apiService.getLatestQuizzes(null, "CreatedAt desc", 5, "Creator($select=Username)")
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ODataResponse<QuizDto>> call, Response<ODataResponse<QuizDto>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<QuizDto> quizDtos = response.body().value;
                            showOrHideRecycler(recyclerQuizzes, R.id.text_no_quizzes, quizDtos);

                            if (quizDtos != null && !quizDtos.isEmpty()) {
                                List<Quiz> quizzes = new ArrayList<>();
                                for (QuizDto dto : quizDtos) {
                                    Quiz quiz = new Quiz();
                                    quiz.quizId = dto.QuizId;
                                    quiz.quizName = dto.QuizName;
                                    quiz.description = dto.Description;
                                    quizzes.add(quiz);
                                }

                                QuizAdapter adapter = new QuizAdapter();
                                adapter.submitList(quizzes);
                                recyclerQuizzes.setAdapter(adapter);
                                adapter.setOnItemClickListener(quiz -> {
                                    Intent intent = new Intent(requireContext(), QuizDetailActivity.class);
                                    intent.putExtra("EXTRA_QUIZ_ID", quiz.quizId);
                                    intent.putExtra("EXTRA_QUIZ_NAME", quiz.quizName);
                                    startActivity(intent);
                                });
                                adapter.setOnDownloadClickListener(quiz -> {
                                    QuizDownloader.downloadQuizWithFlashcards(requireContext(), quiz);
                                });
                            }
                        } else {
                            showErrorText(R.id.text_no_quizzes, "Không tải được dữ liệu.");
                        }
                    }

                    @Override
                    public void onFailure(Call<ODataResponse<QuizDto>> call, Throwable t) {
                        showErrorText(R.id.text_no_quizzes, "Lỗi kết nối.");
                    }
                });
    }

    private <T> void showOrHideRecycler(RecyclerView recyclerView, int emptyViewId, List<T> list) {
        if (list == null || list.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            rootView.findViewById(emptyViewId).setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            rootView.findViewById(emptyViewId).setVisibility(View.GONE);
        }
    }

    private void showErrorText(int viewId, String message) {
        TextView textView = rootView.findViewById(viewId);
        recyclerQuizzes.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(message);
    }
}
