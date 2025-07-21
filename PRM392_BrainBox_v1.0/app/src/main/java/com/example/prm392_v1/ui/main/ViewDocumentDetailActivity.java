package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.prm392_v1.utils.AuthUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDocumentDetailActivity extends AppCompatActivity {
    private TextView txtTitle, txtContent, txtAuthor, txtViews, txtCaption;
    private ImageView imageDetail;
    private RecyclerView recyclerViewComments;
    private EditText editComment;
    private Button btnNext, btnPrevious, btnSubmitComment;
    private ImageButton btnBack;
    private CommentAdapter commentAdapter;
    private ApiService apiService;
    private int docId;
    private List<DocumentDetail> documentDetails;
    private int currentIndex = 0;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ViewDocumentDetailActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_document_detail);

        txtTitle = findViewById(R.id.txt_doc_title);
        txtContent = findViewById(R.id.txt_doc_content);
        txtAuthor = findViewById(R.id.txt_doc_author);
        txtViews = findViewById(R.id.txt_doc_views);
        txtCaption = findViewById(R.id.text_caption);
        imageDetail = findViewById(R.id.image_detail);
        recyclerViewComments = findViewById(R.id.recycler_view_comments);
        editComment = findViewById(R.id.edit_comment);
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        btnSubmitComment = findViewById(R.id.btn_submit_comment);
        btnBack = findViewById(R.id.btn_back);

        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        recyclerViewComments.setAdapter(commentAdapter);

        apiService = RetrofitClient.getApiService(this);
        docId = getIntent().getIntExtra("docId", -1);
        documentDetails = new ArrayList<>();

        loadDocumentDetails();

        btnBack.setOnClickListener(v -> finish());
        btnNext.setOnClickListener(v -> showNextDetail());
        btnPrevious.setOnClickListener(v -> showPreviousDetail());
        btnSubmitComment.setOnClickListener(v -> submitComment());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                } else {
                    Toast.makeText(ViewDocumentDetailActivity.this, "Không thể tải tài liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DocumentDto> call, Throwable t) {
                Toast.makeText(ViewDocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                    Toast.makeText(ViewDocumentDetailActivity.this, "Không thể tải chi tiết tài liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<DocumentDetail>> call, Throwable t) {
                Toast.makeText(ViewDocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ViewDocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitComment() {
        String commentText = editComment.getText().toString().trim();
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
            return;
        }

        if (documentDetails == null || documentDetails.isEmpty()) {
            Toast.makeText(this, "Không có chi tiết tài liệu để bình luận", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentIndex >= 0 && currentIndex < documentDetails.size()) {
            DocumentDetail detail = documentDetails.get(currentIndex);
            Comment newComment = new Comment();
            newComment.DocDetailId = detail.DocDetailId;
            newComment.Content = commentText;
            newComment.CreatedAt = System.currentTimeMillis();

            String userIdStr = AuthUtils.getUserIdFromToken(this);
            if (userIdStr == null) {
                Toast.makeText(this, "Vui lòng đăng nhập để bình luận", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                newComment.UserId = Integer.parseInt(userIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Lỗi: Không thể xác định thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.createComment(newComment).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(ViewDocumentDetailActivity.this, "Đã gửi bình luận", Toast.LENGTH_SHORT).show();
                        editComment.setText("");
                        loadComments(detail.DocDetailId);
                    } else {
                        Toast.makeText(ViewDocumentDetailActivity.this, "Gửi bình luận thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Toast.makeText(ViewDocumentDetailActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Không tìm thấy chi tiết tài liệu để bình luận", Toast.LENGTH_SHORT).show();
        }
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