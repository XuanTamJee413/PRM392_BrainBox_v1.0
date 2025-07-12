package com.example.prm392_v1.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.DocumentActionAdapter;
import com.example.prm392_v1.ui.main.DocumentDetailActivity;
import com.example.prm392_v1.utils.AuthUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocFragment extends Fragment {
    private RecyclerView recyclerView;
    private DocumentActionAdapter documentAdapter;
    private ApiService apiService;
    private TextView tvEmpty;
    private FloatingActionButton fabAddDocument;

    public DocFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_documents);
        tvEmpty = view.findViewById(R.id.tv_empty);
        fabAddDocument = view.findViewById(R.id.fab_add_document);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        documentAdapter = new DocumentActionAdapter();
        recyclerView.setAdapter(documentAdapter);

        apiService = RetrofitClient.getApiService(getContext());

        // Listener for viewing document details
        documentAdapter.setOnItemClickListener(document -> {
            Bundle bundle = new Bundle();
            bundle.putInt("docId", document.DocId);
            DocumentDetailActivity.start(getContext(), bundle);
        });

        // Listener for updating document
        documentAdapter.setOnUpdateClickListener(this::showUpdateDialog);

        // Listener for deleting document
        documentAdapter.setOnDeleteClickListener(document -> deleteDocument(document.DocId));

        // Listener for adding new document
        fabAddDocument.setOnClickListener(v -> showCreateDialog());

        loadDocuments();
        return view;
    }

    private void loadDocuments() {
        String currentUserId = AuthUtils.getUserIdFromToken(getContext());
        if (currentUserId == null || currentUserId.isEmpty()) {
            Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        String filter = "Author/Id eq " + currentUserId;
        apiService.getAllDocuments(filter, "Author").enqueue(new Callback<ODataResponse<DocumentDto>>() {
            @Override
            public void onResponse(@NonNull Call<ODataResponse<DocumentDto>> call, @NonNull Response<ODataResponse<DocumentDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    documentAdapter.submitList(response.body().value);
                    tvEmpty.setVisibility(response.body().value.isEmpty() ? View.VISIBLE : View.GONE);
                    recyclerView.setVisibility(response.body().value.isEmpty() ? View.GONE : View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải tài liệu", Toast.LENGTH_SHORT).show();
                    tvEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ODataResponse<DocumentDto>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                tvEmpty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void showCreateDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Tạo tài liệu mới");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_create_document, null);
        EditText etTitle = dialogView.findViewById(R.id.et_document_title);
        EditText etContent = dialogView.findViewById(R.id.et_document_content);

        builder.setView(dialogView);
        builder.setPositiveButton("Tạo", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tiêu đề và nội dung", Toast.LENGTH_SHORT).show();
                return;
            }

            DocumentDto newDoc = new DocumentDto();
            newDoc.Title = title;
            newDoc.Content = content;
            newDoc.IsPublic = true; // Mặc định công khai
            newDoc.Views = 0;
            newDoc.CreatedAt = System.currentTimeMillis();
            // Không gán Author vì backend sẽ tự lấy từ token

            apiService.createDocument(newDoc).enqueue(new Callback<DocumentDto>() {
                @Override
                public void onResponse(Call<DocumentDto> call, Response<DocumentDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getContext(), "Tạo tài liệu thành công", Toast.LENGTH_SHORT).show();
                        loadDocuments();
                    } else {
                        Toast.makeText(getContext(), "Tạo tài liệu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<DocumentDto> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void showUpdateDialog(DocumentDto document) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Cập nhật tài liệu");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_edit_document, null);
        EditText etTitle = dialogView.findViewById(R.id.et_document_title);
        EditText etContent = dialogView.findViewById(R.id.et_document_content);
        etTitle.setText(document.Title != null ? document.Title : "");
        etContent.setText(document.Content != null ? document.Content : "");

        builder.setView(dialogView);
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tiêu đề và nội dung", Toast.LENGTH_SHORT).show();
                return;
            }

            DocumentDto updatedDoc = new DocumentDto();
            updatedDoc.DocId = document.DocId;
            updatedDoc.Title = title;
            updatedDoc.Content = content;
            updatedDoc.IsPublic = document.IsPublic;
            updatedDoc.Views = document.Views;
            updatedDoc.CreatedAt = document.CreatedAt;
            updatedDoc.Author = document.Author; // Giữ nguyên Author

            apiService.updateDocument(document.DocId, updatedDoc).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Cập nhật tài liệu thành công", Toast.LENGTH_SHORT).show();
                        loadDocuments();
                    } else {
                        Toast.makeText(getContext(), "Cập nhật tài liệu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    private void deleteDocument(int docId) {
        apiService.deleteDocument(docId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xóa tài liệu thành công", Toast.LENGTH_SHORT).show();
                    loadDocuments();
                } else {
                    Toast.makeText(getContext(), "Xóa tài liệu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}