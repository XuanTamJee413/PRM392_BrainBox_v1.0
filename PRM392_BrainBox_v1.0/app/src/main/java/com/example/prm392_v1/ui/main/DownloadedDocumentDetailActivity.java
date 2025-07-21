package com.example.prm392_v1.ui.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.BrainBoxDatabase;
import com.example.prm392_v1.data.entity.DocumentDetail;
import com.example.prm392_v1.ui.adapters.DocumentDetailAdapter;

import java.util.ArrayList;
import java.util.List;

public class DownloadedDocumentDetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DocumentDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_downloaded_document_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView = findViewById(R.id.recycler_downloaded_document_details);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DocumentDetailAdapter();
        recyclerView.setAdapter(adapter);

        int docId = getIntent().getIntExtra("docId", -1);
        if (docId == -1) {
            Toast.makeText(this, "Không tìm thấy tài liệu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        TextView textTitle = findViewById(R.id.text_doc_detail_title);
        String title = getIntent().getStringExtra("title");
        textTitle.setText(title != null ? title : "Doc Id #" + docId);

        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(v -> finish());

        loadDetails(docId);
    }
    private void loadDetails(int docId) {
        new Thread(() -> {
            List<com.example.prm392_v1.data.entity.DocumentDetail> entities =
                    BrainBoxDatabase.getInstance(this).documentDetailDao().getByDocId(docId);

            List<com.example.prm392_v1.data.model.DocumentDetail> models = new ArrayList<>();
            for (com.example.prm392_v1.data.entity.DocumentDetail e : entities) {
                com.example.prm392_v1.data.model.DocumentDetail m = new com.example.prm392_v1.data.model.DocumentDetail();
                m.DocDetailId = e.docDetailId;
                m.DocId = e.docId;
                m.ImageUrl = e.imageUrl;
                m.Caption = e.caption;
                m.CreatedAt = e.createdAt;
                models.add(m);
            }

            runOnUiThread(() -> adapter.submitList(models));
        }).start();
    }

}