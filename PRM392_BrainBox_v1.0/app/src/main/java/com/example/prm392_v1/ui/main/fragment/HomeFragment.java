package com.example.prm392_v1.ui.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.QuizDto;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Gọi API lấy danh sách tài liệu
        TextView textTopDocuments = view.findViewById(R.id.text_top_documents);
        textTopDocuments.setText("Loading...");

        ApiService apiService = RetrofitClient.getApiService(requireContext());
        apiService.getTopDocuments(
                null,
                "Views desc",
                5,
                "Author($select=Username)"
        ).enqueue(new Callback<ODataResponse<DocumentDto>>() {
            @Override
            public void onResponse(Call<ODataResponse<DocumentDto>> call, Response<ODataResponse<DocumentDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DocumentDto> docs = response.body().value;
                    if (docs == null || docs.isEmpty()) {
                        textTopDocuments.setText("Không có tài liệu.");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (DocumentDto doc : docs) {
                            String author = doc.Author != null ? doc.Author.Username : "Ẩn danh";
                            sb.append("• ").append(doc.Title).append(" - ").append(author).append("\n");
                        }
                        textTopDocuments.setText(sb.toString().trim());
                    }
                } else {
                    textTopDocuments.setText("Không tải được dữ liệu.");
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<DocumentDto>> call, Throwable t) {
                textTopDocuments.setText("Lỗi kết nối.");
            }
        });
        TextView textLatestQuizzes = view.findViewById(R.id.text_latest_quizzes);
        textLatestQuizzes.setText("Loading...");

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
                        textLatestQuizzes.setText("Không có quiz.");
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (QuizDto quiz : quizzes) {
                            String creator = quiz.Creator != null ? quiz.Creator.Username : "Ẩn danh";
                            sb.append("• ").append(quiz.QuizName).append(" - ").append(creator).append("\n");
                        }
                        textLatestQuizzes.setText(sb.toString().trim());
                    }
                } else {
                    textLatestQuizzes.setText("Không tải được dữ liệu.");
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<QuizDto>> call, Throwable t) {
                textLatestQuizzes.setText("Lỗi kết nối.");
            }
        });

        return view;
    }
}