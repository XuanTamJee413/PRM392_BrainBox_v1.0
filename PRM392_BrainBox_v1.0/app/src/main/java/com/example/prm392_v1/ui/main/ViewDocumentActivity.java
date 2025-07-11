package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.DocumentAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDocumentActivity extends AppCompatActivity implements DocumentAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private DocumentAdapter documentAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
    private List<DocumentDto> fullDocumentList = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, ViewDocumentActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_document);

        progressBar = findViewById(R.id.progress_bar);
        searchView = findViewById(R.id.search_view);
        Button btnBackHome = findViewById(R.id.btn_back_home);

        setupRecyclerView();
        setupSearchView();

        btnBackHome.setOnClickListener(v -> finish());

        loadDocumentsFromApi();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.document_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        documentAdapter = new DocumentAdapter();
        recyclerView.setAdapter(documentAdapter);
        documentAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(DocumentDto document) {
        Bundle bundle = new Bundle();
        bundle.putInt("docId", document.DocId);
        ViewDocumentDetailActivity.start(this, bundle);
    }

    private void loadDocumentsFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getApiService(this);

        apiService.getAllDocuments(null, "Author").enqueue(new Callback<ODataResponse<DocumentDto>>() {
            @Override
            public void onResponse(Call<ODataResponse<DocumentDto>> call, Response<ODataResponse<DocumentDto>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    fullDocumentList = response.body().value;
                    documentAdapter.submitList(fullDocumentList);
                } else {
                    Toast.makeText(ViewDocumentActivity.this, "Không thể tải dữ liệu. Mã lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<DocumentDto>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewDocumentActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        searchView.clearFocus();
    }

    private void filter(String text) {
        List<DocumentDto> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(fullDocumentList);
        } else {
            for (DocumentDto item : fullDocumentList) {
                if (item.Title != null && item.Title.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        documentAdapter.submitList(filteredList);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (searchView.hasFocus()) {
                int[] location = new int[2];
                searchView.getLocationOnScreen(location);
                int left = location[0];
                int top = location[1];
                int right = left + searchView.getWidth();
                int bottom = top + searchView.getHeight();

                float x = event.getRawX();
                float y = event.getRawY();
                if (x < left || x > right || y < top || y > bottom) {
                    searchView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}