package com.example.prm392_v1.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


import com.example.prm392_v1.data.BrainBoxDatabase;
import com.example.prm392_v1.data.dao.DocumentDao;
import com.example.prm392_v1.data.dao.DocumentDetailDao;
import com.example.prm392_v1.data.entity.Document;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.entity.DocumentDetail;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.auth.LoginActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentDownloader {

    public static void downloadDocumentWithDetails(Context context, DocumentDto dto) {
        String token = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE).getString("jwt_token", null);
        if (token == null || token.isEmpty()) {
            Toast.makeText(context, "Bạn cần đăng nhập trước", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class));
            return;
        }

        new Thread(() -> {
            DocumentDao docDao = BrainBoxDatabase.getInstance(context).documentDao();
            Document old = docDao.getByDocId(dto.DocId);

            saveDocument(context, dto);

            fetchAndSaveDetails(context, dto.DocId, () -> {
                String msg = (old != null)
                        ? "Đã thay thế bản tải xuống trước đó"
                        : "Đã tải xuống tài liệu";

                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show());
            });
        }).start();
    }

    private static void saveDocument(Context context, DocumentDto dto) {
        Document doc = new Document();
        doc.docId = dto.DocId;
        doc.title = dto.Title;
        doc.content = dto.Content;
        doc.authorId = dto.Author != null ? dto.Author.hashCode() : -1;
        doc.isPublic = dto.IsPublic;
        doc.views = dto.Views;
        doc.createdAt = dto.CreatedAt;

        BrainBoxDatabase.getInstance(context).documentDao().insert(doc);
    }

    private static void fetchAndSaveDetails(Context context, int docId, Runnable onSuccess) {
        ApiService api = RetrofitClient.getApiService(context);
        String filter = "DocId eq " + docId;

        api.getDocumentDetails(filter).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ODataResponse<com.example.prm392_v1.data.model.DocumentDetail>> call,
                                   Response<ODataResponse<com.example.prm392_v1.data.model.DocumentDetail>> response) {

                if (response.code() == 401) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Toast.makeText(context, "Bạn cần đăng nhập trước", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, LoginActivity.class));
                    });
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    List<com.example.prm392_v1.data.model.DocumentDetail> details = response.body().value;

                    new Thread(() -> {
                        DocumentDetailDao dao = BrainBoxDatabase.getInstance(context).documentDetailDao();

                        dao.deleteByDocId(docId); // Xoá hết cũ trước

                        for (com.example.prm392_v1.data.model.DocumentDetail d : details) {
                            DocumentDetail entity = new DocumentDetail();
                            entity.docDetailId = d.DocDetailId;
                            entity.docId = d.DocId;
                            entity.caption = d.Caption;
                            entity.imageUrl = d.ImageUrl;
                            entity.createdAt = d.CreatedAt;

                            dao.insert(entity);
                        }
                        onSuccess.run();
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<com.example.prm392_v1.data.model.DocumentDetail>> call, Throwable t) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(context, "Lỗi tải chi tiết tài liệu", Toast.LENGTH_SHORT).show());
            }
        });
    }
}