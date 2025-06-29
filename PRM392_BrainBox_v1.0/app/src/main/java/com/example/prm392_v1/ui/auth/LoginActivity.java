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

import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashed = hash(password);

            new Thread(() -> {
                User user = BrainBoxDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .login(username, hashed);

                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(this, "Đăng nhập thành công! Role: " + user.role, Toast.LENGTH_SHORT).show();

                        if ("admin".equalsIgnoreCase(user.role)) {
                            startActivity(new Intent(this, DashboardActivity.class));
                        } else if ("teacher".equalsIgnoreCase(user.role)) {
                            startActivity(new Intent(this, HomeActivity.class));
                        } else{
                            startActivity(new Intent(this, MainActivity.class));
                        }

                        finish();
                    } else {
                        Toast.makeText(this, "Sai thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        });
    }
    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return "";
        }
    }
}