package com.example.prm392_v1.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_v1.R; // Import R để truy cập layout
import com.example.prm392_v1.ui.auth.LoginActivity;
import com.example.prm392_v1.utils.AuthUtils;
import com.example.prm392_v1.utils.NotificationScheduler;

public class SplashActivity extends AppCompatActivity {

    // Thời gian hiển thị màn hình chờ (2000ms = 2 giây)
    private static final int SPLASH_DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // **SỬA LỖI QUAN TRỌNG:** Phải đặt layout để hiển thị giao diện
        setContentView(R.layout.activity_splash);

        // Sử dụng Handler để tạo độ trễ trước khi chuyển màn hình
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Logic bên trong sẽ được thực thi sau khi hết thời gian SPLASH_DELAY

            // Đặt lịch nhắc nhở hàng ngày khi ứng dụng khởi chạy
            NotificationScheduler.setDailyReminders(this);

            // Kiểm tra token đã lưu
            String token = AuthUtils.getToken(this);

            if (token != null && !token.isEmpty()) {
                // Nếu có token, điều hướng dựa trên vai trò người dùng
                AuthUtils.navigateToScreenByRole(this, token);
            } else {
                // Nếu không có token, chuyển đến màn hình Đăng nhập
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }

            // Đóng SplashActivity để người dùng không thể quay lại màn hình này
            finish();

        }, SPLASH_DELAY);
    }
}