package com.example.prm392_v1.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.UserDto;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.adapters.UserAdapter;
import com.example.prm392_v1.ui.auth.LoginActivity;
import com.example.prm392_v1.utils.AuthUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {

    private RecyclerView rvUsers;
    private ProgressBar progressBar;
    private UserAdapter userAdapter;
    private List<UserDto> userList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvUsers = findViewById(R.id.rvUsers);
        progressBar = findViewById(R.id.progressBar);
        apiService = RetrofitClient.getApiService(this);

        setupRecyclerView();
        fetchUsers();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Gắn layout menu (dashboard_menu.xml) vào action bar
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            performLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performLogout() {
        AuthUtils.clearToken(this);
        Intent intent = new Intent(this, LoginActivity.class);
        // Xóa tất cả các activity trước đó khỏi stack để người dùng không thể quay lại
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupRecyclerView() {
        userAdapter = new UserAdapter(userList, this);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(userAdapter);
    }

    private void fetchUsers() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getAllUsers().enqueue(new Callback<ODataResponse<UserDto>>() {
            @Override
            public void onResponse(@NonNull Call<ODataResponse<UserDto>> call, @NonNull Response<ODataResponse<UserDto>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body().getValue());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DashboardActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ODataResponse<UserDto>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DashboardActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onChangeRoleClicked(UserDto user) {
        final String[] roles = {"user", "teacher", "admin"};
        new AlertDialog.Builder(this)
                .setTitle("Change Role for " + user.Username)
                .setItems(roles, (dialog, which) -> {
                    String newRole = roles[which];
                    if (!user.Role.equals(newRole)) {
                        updateUser(user.Id, "Role", newRole);
                    }
                })
                .show();
    }

    @Override
    public void onBlockStatusChanged(UserDto user) {
        boolean newStatus = !user.Status;
        String action = newStatus ? "Unblock" : "Block";

        new AlertDialog.Builder(this)
                .setTitle(action + " User")
                .setMessage("Are you sure you want to " + action.toLowerCase() + " " + user.Username + "?")
                .setPositiveButton("Yes", (dialog, which) -> updateUser(user.Id, "Status", newStatus))
                .setNegativeButton("No", null)
                .show();
    }

    private void updateUser(int userId, String key, Object value) {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, Object> updates = new HashMap<>();
        updates.put(key, value);

        apiService.updateUser(userId, updates).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DashboardActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    fetchUsers(); // Tải lại danh sách người dùng để cập nhật giao diện
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(DashboardActivity.this, "Update failed. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DashboardActivity.this, "Update error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
