package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.Comment;
import com.example.prm392_v1.data.model.DocumentDetail;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.CommentAdapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDetailActivity extends AppCompatActivity {
    private TextView txtTitle, txtContent, txtAuthor, txtViews, txtCaption;
    private ImageView imageDetail;
    private RecyclerView recyclerViewComments;
    private CommentAdapter commentAdapter;
    private Button btnUpdate, btnDelete, btnNext, btnPrevious;
    private ImageButton btnBack;
    private ApiService apiService;
    private int docId;
    private List<DocumentDetail> documentDetails;
    private int currentIndex = 0;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, DocumentDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_detail);

        // Khởi tạo các thành phần giao diện
        txtTitle = findViewById(R.id.txt_doc_title);
        txtContent = findViewById(R.id.txt_doc_content);
        txtAuthor = findViewById(R.id.txt_doc_author);
        txtViews = findViewById(R.id.txt_doc_views);
        txtCaption = findViewById(R.id.text_caption);
        imageDetail = findViewById(R.id.image_detail);
        recyclerViewComments = findViewById(R.id.recycler_view_comments);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        btnBack = findViewById(R.id.btn_back);

        // Thiết lập RecyclerView cho bình luận
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        recyclerViewComments.setAdapter(commentAdapter);

        // Khởi tạo ApiService và lấy docId từ Intent
        apiService = RetrofitClient.getApiService(this);
        docId = getIntent().getIntExtra("docId", -1);
        documentDetails = new ArrayList<>();

        // Tải dữ liệu tài liệu và chi tiết
        loadDocumentDetails();

        // Xử lý sự kiện nút
        btnUpdate.setOnClickListener(v -> updateDocumentDetail());
        btnDelete.setOnClickListener(v -> deleteDocumentDetail());
        btnBack.setOnClickListener(v -> finish());
        btnNext.setOnClickListener(v -> showNextDetail());
        btnPrevious.setOnClickListener(v -> showPreviousDetail());

        // Xử lý WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadDocumentDetails() {
        // Tải thông tin tài liệu
        apiService.getDocumentById(docId, "Author").enqueue(new Callback<DocumentDto>() {
            @Override
            public void onResponse(Call<DocumentDto> call, Response<DocumentDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DocumentDto doc = response.body();
                    txtTitle.setText(doc.Title);
                    txtContent.setText(doc.Content);
                    txtAuthor.setText(doc.Author != null ? doc.Author.Username : "Ẩn danh");
                    txtViews.setText(doc.Views + " lượt xem");
                } else {
                    Toast.makeText(DocumentDetailActivity.this, "Không thể tải tài liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DocumentDto> call, Throwable t) {
                Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Tải danh sách DocumentDetail
        apiService.getDocumentDetails("docId eq " + docId).enqueue(new Callback<ODataResponse<DocumentDetail>>() {
            @Override
            public void onResponse(Call<ODataResponse<DocumentDetail>> call, Response<ODataResponse<DocumentDetail>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    documentDetails = response.body().value;
                    if (!documentDetails.isEmpty()) {
                        updateCurrentDetail();
                    } else {
                        txtCaption.setText("");
                        imageDetail.setImageResource(R.drawable.ic_placeholder);
                        commentAdapter.submitList(new ArrayList<>());
                    }
                    updateButtonVisibility();
                } else {
                    Toast.makeText(DocumentDetailActivity.this, "Không thể tải chi tiết tài liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<DocumentDetail>> call, Throwable t) {
                Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCurrentDetail() {
        if (currentIndex >= 0 && currentIndex < documentDetails.size()) {
            DocumentDetail detail = documentDetails.get(currentIndex);
            txtCaption.setText(detail.Caption != null ? detail.Caption : "");
            if (detail.ImageUrl != null && !detail.ImageUrl.isEmpty()) {
                new LoadImageTask(imageDetail).execute(detail.ImageUrl);
            } else {
                imageDetail.setImageResource(R.drawable.ic_placeholder);
            }
            loadComments(detail.DocDetailId);
        }
        updateButtonVisibility();
    }

    private void loadComments(int docDetailId) {
        apiService.getCommentsByDocDetail("DocumentDetail/DocDetailId eq " + docDetailId).enqueue(new Callback<ODataResponse<Comment>>() {
            @Override
            public void onResponse(Call<ODataResponse<Comment>> call, Response<ODataResponse<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    commentAdapter.submitList(response.body().value);
                } else {
                    commentAdapter.submitList(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<Comment>> call, Throwable t) {
                Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNextDetail() {
        if (currentIndex < documentDetails.size() - 1) {
            currentIndex++;
            updateCurrentDetail();
        }
    }

    private void showPreviousDetail() {
        if (currentIndex > 0) {
            currentIndex--;
            updateCurrentDetail();
        }
    }

    private void updateButtonVisibility() {
        btnPrevious.setVisibility(currentIndex > 0 ? View.VISIBLE : View.GONE);
        btnNext.setVisibility(currentIndex < documentDetails.size() - 1 ? View.VISIBLE : View.GONE);
    }

    private void updateDocumentDetail() {
        if (currentIndex >= 0 && currentIndex < documentDetails.size()) {
            DocumentDetail updatedDetail = documentDetails.get(currentIndex);
            updatedDetail.Caption = txtCaption.getText().toString();

            apiService.updateDocumentDetail(updatedDetail.DocDetailId, updatedDetail).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(DocumentDetailActivity.this, "Cập nhật chi tiết tài liệu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DocumentDetailActivity.this, "Cập nhật chi tiết tài liệu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DocumentDetailActivity.this, "Không có chi tiết tài liệu để cập nhật", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteDocumentDetail() {
        if (currentIndex >= 0 && currentIndex < documentDetails.size()) {
            DocumentDetail detail = documentDetails.get(currentIndex);
            apiService.deleteDocumentDetail(detail.DocDetailId).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        documentDetails.remove(currentIndex);
                        if (!documentDetails.isEmpty()) {
                            if (currentIndex >= documentDetails.size()) {
                                currentIndex--;
                            }
                            updateCurrentDetail();
                        } else {
                            txtCaption.setText("");
                            imageDetail.setImageResource(R.drawable.ic_placeholder);
                            commentAdapter.submitList(new ArrayList<>());
                            updateButtonVisibility();
                        }
                        Toast.makeText(DocumentDetailActivity.this, "Xóa chi tiết tài liệu thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DocumentDetailActivity.this, "Xóa chi tiết tài liệu thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(DocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DocumentDetailActivity.this, "Không có chi tiết tài liệu để xóa", Toast.LENGTH_SHORT).show();
        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                input.close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.ic_placeholder);
            }
        }
    }
}