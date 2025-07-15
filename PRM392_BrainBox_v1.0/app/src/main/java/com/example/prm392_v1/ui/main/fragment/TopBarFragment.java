package com.example.prm392_v1.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.example.prm392_v1.R;
import com.example.prm392_v1.ui.main.SearchResultActivity;

public class TopBarFragment extends Fragment {

    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus(); // Ngăn keyboard tự động mở

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Giao tiếp với Activity để xử lý tìm kiếm
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // Activity có thể gọi phương thức này nếu cần
    public SearchView getSearchView() {
        return searchView;
    }
}