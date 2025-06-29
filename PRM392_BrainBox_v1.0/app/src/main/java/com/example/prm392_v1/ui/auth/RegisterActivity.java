package com.example.prm392_v1.ui.auth;

import static com.example.prm392_v1.ui.auth.LoginActivity.hash;

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

public class RegisterActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword, edtConfirmPassword;
    Button btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
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

            String hashed = hash(password);

            new Thread(() -> {
                User existing = BrainBoxDatabase.getInstance(getApplicationContext())
                        .userDao()
                        .getByUsername(username);

                if (existing != null) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    User user = new User(username, hashed, "user");
                    BrainBoxDatabase.getInstance(getApplicationContext())
                            .userDao()
                            .insert(user);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Đăng ký thành công! Role: " + user.role, Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            }).start();
        });
    }
}