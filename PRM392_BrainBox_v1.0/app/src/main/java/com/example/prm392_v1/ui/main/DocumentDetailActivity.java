package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDetail;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.DocumentDetailAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDetailActivity extends AppCompatActivity {
    private TextView txtTitle, txtContent, txtAuthor, txtViews;
    private RecyclerView recyclerViewDetails;
    private DocumentDetailAdapter detailAdapter;
    private ApiService apiService;
    private int docId;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DocumentDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);

        txtTitle = findViewById(R.id.txt_doc_title);
        txtContent = findViewById(R.id.txt_doc_content);
        txtAuthor = findViewById(R.id.txt_doc_author);
        txtViews = findViewById(R.id.txt_doc_views);
        recyclerViewDetails = findViewById(R.id.recycler_view_details);
        Button btnUpdate = findViewById(R.id.btn_update);
        Button btnDelete = findViewById(R.id.btn_delete);
        ImageButton btnBack = findViewById(R.id.btn_back);

        recyclerViewDetails.setLayoutManager(new LinearLayoutManager(this));
        detailAdapter = new DocumentDetailAdapter();
        recyclerViewDetails.setAdapter(detailAdapter);

        apiService = RetrofitClient.getApiService(this);
        docId = getIntent().getIntExtra("docId", -1);

        loadDocumentDetails();

        btnUpdate.setOnClickListener(v -> updateDocument());
        btnDelete.setOnClickListener(v -> deleteDocument());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadDocumentDetails() {
        apiService.getDocumentById(docId, "Author").enqueue(new Callback<DocumentDto>() {
            @Override
            public void onResponse(Call<DocumentDto> call, Response<DocumentDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DocumentDto doc = response.body();
                    txtTitle.setText(doc.Title);
                    txtContent.setText(doc.Content);
                    txtAuthor.setText(doc.Author != null ? doc.Author.Username : "Ẩn danh");
                    txtViews.setText(doc.Views + " lượt xem");
                }
            }

            @Override
            public void onFailure(Call<DocumentDto> call, Throwable t) {
                Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        apiService.getDocumentDetails("docId eq " + docId).enqueue(new Callback<ODataResponse<DocumentDetail>>() {
            @Override
            public void onResponse(Call<ODataResponse<DocumentDetail>> call, Response<ODataResponse<DocumentDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    detailAdapter.submitList(response.body().value);
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<DocumentDetail>> call, Throwable t) {
                Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDocument() {
        DocumentDto updatedDoc = new DocumentDto();
        updatedDoc.DocId = docId;
        updatedDoc.Title = txtTitle.getText().toString();
        updatedDoc.Content = txtContent.getText().toString();

        apiService.updateDocument(docId, updatedDoc).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DocumentDetailActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DocumentDetailActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteDocument() {
        apiService.deleteDocument(docId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DocumentDetailActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(DocumentDetailActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}