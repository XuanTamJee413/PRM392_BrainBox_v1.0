package com.example.prm392_v1.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392_v1.ui.auth.LoginActivity;
import com.example.prm392_v1.utils.AuthUtils;
import com.example.prm392_v1.utils.NotificationScheduler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Đặt lịch nhắc nhở hàng ngày khi ứng dụng khởi chạy
        NotificationScheduler.setDailyReminders(this);

        // Optional: Add a short delay for splash screen effect
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            String token = AuthUtils.getToken(this);

            if (token != null && !token.isEmpty()) {
                AuthUtils.navigateToScreenByRole(this, token);
            } else {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 1000); // 1-second delay for splash effect
    }
}