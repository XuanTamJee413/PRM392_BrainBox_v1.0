package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        // Initialize SearchView
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // Clear initial focus to prevent keyboard from opening automatically
        searchView.clearFocus();

        // Initialize BottomNavigationView
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

        // Set default fragment
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (searchView.hasFocus()) {
                // Get the SearchView's bounds
                int[] location = new int[2];
                searchView.getLocationOnScreen(location);
                int left = location[0];
                int top = location[1];
                int right = left + searchView.getWidth();
                int bottom = top + searchView.getHeight();

                // Check if the touch is outside the SearchView
                float x = event.getRawX();
                float y = event.getRawY();
                if (x < left || x > right || y < top || y > bottom) {
                    // Clear focus and hide keyboard
                    searchView.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}