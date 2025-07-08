package com.example.prm392_v1.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.BrainBoxDatabase;
import com.example.prm392_v1.data.entity.User;
import com.example.prm392_v1.ui.main.DashboardActivity;
import com.example.prm392_v1.ui.main.HomeActivity;
import com.example.prm392_v1.ui.main.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.model.LoginRequest;
import com.example.prm392_v1.data.model.LoginResponse;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.main.MainActivity;
import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {
//    EditText edtUsername, edtPassword;
//    Button btnLogin;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        edtUsername = findViewById(R.id.edtUsername);
//        edtPassword = findViewById(R.id.edtPassword);
//        btnLogin = findViewById(R.id.btnLogin);
//
//        btnLogin.setOnClickListener(v -> {
//            String username = edtUsername.getText().toString().trim();
//            String password = edtPassword.getText().toString().trim();
//
//            if (username.isEmpty() || password.isEmpty()) {
//                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String hashed = hash(password);
//
//            new Thread(() -> {
//                User user = BrainBoxDatabase.getInstance(getApplicationContext())
//                        .userDao()
//                        .login(username, hashed);
//
//                runOnUiThread(() -> {
//                    if (user != null) {
//                        Toast.makeText(this, "Đăng nhập thành công! Role: " + user.role, Toast.LENGTH_SHORT).show();
//
//                        if ("admin".equalsIgnoreCase(user.role)) {
//                            startActivity(new Intent(this, DashboardActivity.class));
//                        } else if ("teacher".equalsIgnoreCase(user.role)) {
//                            startActivity(new Intent(this, HomeActivity.class));
//                        } else{
//                            startActivity(new Intent(this, MainActivity.class));
//                        }
//
//                        finish();
//                    } else {
//                        Toast.makeText(this, "Sai thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }).start();
//        });
//    }
//    public static String hash(String input) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hash = md.digest(input.getBytes("UTF-8"));
//            StringBuilder sb = new StringBuilder();
//            for (byte b : hash) sb.append(String.format("%02x", b));
//            return sb.toString();
//        } catch (Exception e) {
//            return "";
//        }
//    }
EditText edtUsername, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> handleApiLogin());
    }

    private void handleApiLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getApiService(this);
        LoginRequest loginRequest = new LoginRequest(username, password);

        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().token;
                    saveToken(token); // Lưu token lại

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang MainActivity sau khi đăng nhập thành công
                    // Logic phân quyền admin/teacher/student có thể xử lý sau khi có token
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish(); // Đóng màn hình đăng nhập
                } else {
                    // === THÊM DEBUG VÀO ĐÂY ===
                    String errorBody = "Không có thông tin lỗi";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String message = "Đăng nhập thất bại. Mã lỗi: " + response.code() + "\nLý do: " + errorBody;
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences prefs = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        prefs.edit().putString("jwt_token", token).apply();
    }

}