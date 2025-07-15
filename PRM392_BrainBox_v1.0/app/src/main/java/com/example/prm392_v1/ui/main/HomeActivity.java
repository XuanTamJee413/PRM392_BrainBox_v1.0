package com.example.prm392_v1.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import com.example.prm392_v1.R;
import com.example.prm392_v1.ui.main.fragment.ChatAiDialogFragment; // THÊM IMPORT
import com.example.prm392_v1.ui.main.fragment.DocFragment;
import com.example.prm392_v1.ui.main.fragment.DownloadFragment;
import com.example.prm392_v1.ui.main.fragment.HomeFragment;
import com.example.prm392_v1.ui.main.fragment.QuizFragment;
import com.example.prm392_v1.ui.main.fragment.ProfileFragment; // SỬA IMPORT
import com.example.prm392_v1.ui.main.fragment.TopBarFragment;
import com.example.prm392_v1.ui.main.fragment.BottomNavFragment;
import com.example.prm392_v1.ui.views.DraggableFloatingActionButton; // THÊM IMPORT

public class HomeActivity extends AppCompatActivity implements BottomNavFragment.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        DraggableFloatingActionButton fabAi = findViewById(R.id.fab_ai_assistant);
        fabAi.setOnClickListener(view -> {
            ChatAiDialogFragment dialogFragment = new ChatAiDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "ChatAiDialog");
        });
    }

    @Override
    public void onNavigationItemSelected(int itemId) {
        Fragment selectedFragment = null;
        if (itemId == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (itemId == R.id.nav_doc) {
            selectedFragment = new DocFragment();
        } else if (itemId == R.id.nav_quiz) {
            selectedFragment = new QuizFragment();
        } else if (itemId == R.id.nav_download) {
            selectedFragment = new DownloadFragment();
        } else if (itemId == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        }

        if (selectedFragment != null) {
            replaceFragment(selectedFragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content_container, fragment)
                .commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Lấy SearchView từ TopBarFragment
            TopBarFragment topBarFragment = (TopBarFragment) getSupportFragmentManager().findFragmentById(R.id.top_bar_container);
            if (topBarFragment != null) {
                SearchView searchView = topBarFragment.getSearchView();
                if (searchView != null && searchView.hasFocus()) {
                    int[] location = new int[2];
                    searchView.getLocationOnScreen(location);
                    int left = location[0];
                    int top = location[1];
                    int right = left + searchView.getWidth();
                    int bottom = top + searchView.getHeight();

                    if (event.getRawX() < left || event.getRawX() > right || event.getRawY() < top || event.getRawY() > bottom) {
                        searchView.clearFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}