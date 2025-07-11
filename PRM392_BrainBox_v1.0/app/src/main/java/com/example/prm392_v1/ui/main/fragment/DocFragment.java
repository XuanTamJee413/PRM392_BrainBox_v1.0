package com.example.prm392_v1.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.DocumentAdapter;
import com.example.prm392_v1.ui.main.DocumentDetailActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocFragment extends Fragment {
    private RecyclerView recyclerView;
    private DocumentAdapter documentAdapter;
    private ApiService apiService;

    public DocFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doc, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_documents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        documentAdapter = new DocumentAdapter();
        recyclerView.setAdapter(documentAdapter);

        apiService = RetrofitClient.getApiService(getContext());

        documentAdapter.setOnItemClickListener(document -> {
            // Chuyển đến DocumentDetailActivity
            Bundle bundle = new Bundle();
            bundle.putInt("docId", document.DocId);
            DocumentDetailActivity.start(getContext(), bundle);
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
}