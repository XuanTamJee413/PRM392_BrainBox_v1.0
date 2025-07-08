package com.example.prm392_v1.ui.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.prm392_v1.R;
import com.example.prm392_v1.ui.main.fragment.DocFragment;
import com.example.prm392_v1.ui.main.fragment.DownloadFragment;
import com.example.prm392_v1.ui.main.fragment.HomeFragment;
import com.example.prm392_v1.ui.main.fragment.QuizFragment;
import com.example.prm392_v1.ui.main.fragment.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_doc) {
                replaceFragment(new DocFragment());
                return true;
            } else if (itemId == R.id.nav_quiz) {
                replaceFragment(new QuizFragment());
                return true;
            } else if (itemId == R.id.nav_download) {
                replaceFragment(new DownloadFragment());
                return true;
            } else if (itemId == R.id.nav_setting) {
                replaceFragment(new SettingFragment());
                return true;
            } else {
                return false;
            }
        });

        bottomNav.setSelectedItemId(R.id.nav_home);
    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
    }
