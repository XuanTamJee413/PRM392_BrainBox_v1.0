package com.example.prm392_v1.ui.auth;

//import static com.example.prm392_v1.ui.auth.LoginActivity.hash;

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
import com.example.prm392_v1.data.model.LoginResponse;
import com.example.prm392_v1.data.model.RegisterRequest;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.main.HomeActivity;
import com.example.prm392_v1.utils.AuthUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtConfirmPassword;
    Button btnRegister, btnHome, btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnHome = findViewById(R.id.btnHome);
        btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(v -> handleRegister());
        btnHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegister() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(username, password);

        ApiService apiService = RetrofitClient.getApiService(this);
        apiService.register(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().token;
                    AuthUtils.saveToken(RegisterActivity.this, token);
                    AuthUtils.navigateToScreenByRole(RegisterActivity.this, token);
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}