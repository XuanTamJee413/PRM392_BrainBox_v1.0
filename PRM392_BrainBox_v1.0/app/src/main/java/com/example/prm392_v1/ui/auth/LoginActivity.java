package com.example.prm392_v1.ui.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import org.json.JSONObject;

import java.io.Console;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Scanner;

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

            loginViaApi(username, password);
        });
    }
    private void loginViaApi(String username, String rawPassword) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5099/api/Auth/login");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String json = new JSONObject()
                        .put("usernameOrEmail", username)
                        .put("password", rawPassword)
                        .toString();

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(json.getBytes());
                    os.flush();
                }

                int responseCode = conn.getResponseCode();
                InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
                Scanner scanner = new Scanner(is).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                Log.d("API_RESPONSE", "Code: " + responseCode + " | Body: " + response);

                if (responseCode == 200) {
                    String token = new JSONObject(response).getString("token");
                    SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
                    prefs.edit().putString("token", token).apply();

                    String role = parseRoleFromJwt(token);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Login thành công API! Role: " + role, Toast.LENGTH_SHORT).show();
                        navigateTo(role);
                    });
                } else {
                    loginLocal(username, hash(rawPassword), "Không kết nối được máy chủ: " + response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("LOGIN_API_ERROR", "Lỗi gọi API đăng nhập>>>>>>>>>>>>>: " + ex.getMessage(), ex);
                loginLocal(username, hash(rawPassword), "Lỗi mạng hoặc không kết nối được API");
            }
        }).start();
    }


    private void loginLocal(String username, String hashed, String message) {
        new Thread(() -> {
            User user = BrainBoxDatabase.getInstance(getApplicationContext())
                    .userDao()
                    .login(username, hashed);

            runOnUiThread(() -> {
                if (user != null) {
                    Toast.makeText(this, message + ", local = Role: " + user.role, Toast.LENGTH_LONG).show();
                    navigateTo(user.role);
                } else {
                    Toast.makeText(this, "Sai thông tin đăng nhập (API + Local)", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void navigateTo(String role) {
        Intent intent;
        if ("admin".equalsIgnoreCase(role)) {
            intent = new Intent(this, DashboardActivity.class);
        } else if ("teacher".equalsIgnoreCase(role)) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
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
    private String parseRoleFromJwt(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) return "user";

            byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE);
            String payload = new String(decodedBytes);

            JSONObject obj = new JSONObject(payload);
            return obj.getString("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
        } catch (Exception e) {
            return "user";
        }
    }
}