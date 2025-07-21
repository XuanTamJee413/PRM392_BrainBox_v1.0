package com.example.prm392_v1.ui.main;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.entity.User;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private TextView textPrice;
    private Button btnPay, btnHome;
    private long accountCreatedAt = 0L;
    private final long THIRTY_DAYS_MS = 30L * 24 * 60 * 60 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        radioGroup = findViewById(R.id.radio_group);
        textPrice = findViewById(R.id.text_price);
        btnPay = findViewById(R.id.btn_pay);
        btnHome = findViewById(R.id.btn_home);

        btnHome.setOnClickListener(v -> {
            finish();
        });

        accountCreatedAt = getSharedPreferences("auth_prefs", MODE_PRIVATE)
                .getLong("created_at", 0);
        String selectedPackage = getIntent().getStringExtra("selected_package");
        if (selectedPackage != null) {
            switch (selectedPackage) {
                case "lifetime":
                    radioGroup.check(R.id.radio_lifetime);
                    break;
                case "30days":
                    radioGroup.check(R.id.radio_30days);
                    break;
                case "6months":
                    radioGroup.check(R.id.radio_6months);
                    break;
                case "12months":
                    radioGroup.check(R.id.radio_12months);
                    break;
            }
        } else {
            radioGroup.check(R.id.radio_30days);
        }
        updatePrice();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> updatePrice());

        btnPay.setOnClickListener(v -> {
            String selected = getSelectedOption();
            // chua goi dc vn pay fall back tam la da dang ky dc
            long millis = 0;
            if (selected.contains("30 ngày")) millis = 32L * 24 * 60 * 60 * 1000;
            else if (selected.contains("6 tháng")) millis = 182L * 24 * 60 * 60 * 1000;
            else if (selected.contains("12 tháng")) millis = 367L * 24 * 60 * 60 * 1000;
            else if (selected.contains("vĩnh viễn")) millis = 1463L * 365L * 24 * 60 * 60 * 1000;

            String token = getSharedPreferences("auth_prefs", MODE_PRIVATE).getString("jwt_token", null);
            User user = JwtUtils.parseUserFromToken(token);

            if (user == null) {
                Toast.makeText(this, "Không lấy được thông tin người dùng", Toast.LENGTH_SHORT).show();
                return;
            }

            updatePremiumTime(user.id, millis);
        });

    }
    private void updatePrice() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) return;

        RadioButton selected = findViewById(selectedId);
        if (selected == null) return;

        String text = selected.getText().toString();
        if (text.contains("vĩnh viễn")) {
            if (isNewUser()) {
                textPrice.setText("Giá: 89.000đ (vĩnh viễn)");
            } else {
                textPrice.setText("Đã hết ưu đãi thành viên mới");
                btnPay.setEnabled(false);
                btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#BDBDBD")));
                return;
            }
        } else if (text.contains("30 ngày")) {
            textPrice.setText("Giá: 10.000đ");
        } else if (text.contains("6 tháng")) {
            textPrice.setText("Giá: 49.000đ");
        } else if (text.contains("12 tháng")) {
            textPrice.setText("Giá: 99.000đ");
        }

        btnPay.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F57C00")));
        btnPay.setEnabled(true);
    }

    private String getSelectedOption() {
        RadioButton selected = findViewById(radioGroup.getCheckedRadioButtonId());
        return selected.getText().toString();
    }

    private boolean isNewUser() {
        long now = System.currentTimeMillis();
        return (now - accountCreatedAt) < THIRTY_DAYS_MS;
    }
    private void updatePremiumTime(int userId, long addedMillis) {
        long now = System.currentTimeMillis();
        long expiredAt = now + addedMillis;

        ApiService apiService = RetrofitClient.getApiService(this);
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("PremiumExpiredAt", expiredAt);

        apiService.updatePremium(userId, updateMap).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PurchaseActivity.this, "Đăng ký Premium thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PurchaseActivity.this, "Lỗi đăng ký Premium", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PurchaseActivity.this, "Không thể kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

}