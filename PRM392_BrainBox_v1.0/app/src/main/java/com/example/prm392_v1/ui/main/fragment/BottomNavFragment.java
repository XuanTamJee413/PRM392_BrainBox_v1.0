package com.example.prm392_v1.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.prm392_v1.R;

public class BottomNavFragment extends Fragment {

    public interface OnNavigationItemSelectedListener {
        void onNavigationItemSelected(int itemId);
    }

    private OnNavigationItemSelectedListener listener;
    private BottomNavigationView bottomNav;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNavigationItemSelectedListener) {
            listener = (OnNavigationItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnNavigationItemSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottomNav = view.findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            listener.onNavigationItemSelected(item.getItemId());
            return true;
        });

        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}