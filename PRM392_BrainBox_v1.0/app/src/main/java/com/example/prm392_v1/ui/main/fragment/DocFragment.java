package com.example.prm392_v1.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocFragment extends Fragment {
    private RecyclerView recyclerView;
    private DocumentActionAdapter documentAdapter;
    private ApiService apiService;

    public DocFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_documents);
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
        documentAdapter.setOnUpdateClickListener(document -> {
            showUpdateDialog(document);
        });

        // Listener for deleting document
        documentAdapter.setOnDeleteClickListener(document -> {
            deleteDocument(document.DocId);
        });

        loadDocuments();
        return view;
    }

    private void loadDocuments() {
        apiService.getAllDocuments(null, "Author").enqueue(new Callback<ODataResponse<DocumentDto>>() {
            @Override
            public void onResponse(@NonNull Call<ODataResponse<DocumentDto>> call, @NonNull Response<ODataResponse<DocumentDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    documentAdapter.submitList(response.body().value);
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tải tài liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ODataResponse<DocumentDto>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog(DocumentDto document) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Cập nhật tài liệu");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_update_document, null);
        EditText etTitle = dialogView.findViewById(R.id.et_document_title);
        EditText etContent = dialogView.findViewById(R.id.et_document_content);
        etTitle.setText(document.Title);
        etContent.setText(document.Content);

        builder.setView(dialogView);
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            DocumentDto updatedDoc = new DocumentDto();
            updatedDoc.DocId = document.DocId;
            updatedDoc.Title = etTitle.getText().toString();
            updatedDoc.Content = etContent.getText().toString();

            apiService.updateDocument(document.DocId, updatedDoc).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadDocuments(); // Refresh the list
                    } else {
                        Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    loadDocuments(); // Refresh the list
                } else {
                    Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}