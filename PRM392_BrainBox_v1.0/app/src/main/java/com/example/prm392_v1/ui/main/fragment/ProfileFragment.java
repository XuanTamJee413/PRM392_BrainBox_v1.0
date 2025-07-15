package com.example.prm392_v1.ui.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_v1.R;
import com.example.prm392_v1.data.entity.User;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.UserDto;
import com.example.prm392_v1.data.network.ApiService;
import com.example.prm392_v1.data.network.RetrofitClient;
import com.example.prm392_v1.ui.auth.ChangePasswordActivity;
import com.example.prm392_v1.ui.auth.LoginActivity;
import com.example.prm392_v1.ui.main.DashboardActivity;
import com.example.prm392_v1.utils.JwtUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private TextView txtUsername, txtEmail, txtRole, txtStatus, txtPremium, txtCreatedAt;
    private Button btnDashboard, btnChangePassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences prefs = requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);
        if (token == null || token.isEmpty()) {
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return view;
        }

        User user = JwtUtils.parseUserFromToken(token);
        if (user == null) {
            Toast.makeText(requireContext(), "Không đọc được thông tin người dùng từ token", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
            return view;
        }

        txtUsername = view.findViewById(R.id.txtUsername);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtCreatedAt = view.findViewById(R.id.txtCreatedAt);
        txtRole = view.findViewById(R.id.txtRole);
        txtStatus = view.findViewById(R.id.txtStatus);
        txtPremium = view.findViewById(R.id.txtPremium);
        btnDashboard = view.findViewById(R.id.btnDashboard);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        Button btnLogout = view.findViewById(R.id.btnLogout);

        loadUserInfo(user.id);

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.remove("jwt_token");
            editor.apply();
            Toast.makeText(requireContext(), "Đăng xuất", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(requireContext(), LoginActivity.class));
            requireActivity().finish();
        });

        btnChangePassword.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), ChangePasswordActivity.class));
        });

        return view;
    }

    private void loadUserInfo(int userId) {
        ApiService apiService = RetrofitClient.getApiService(requireContext());
        String filter = "Id eq " + userId;

        apiService.getUserById(filter).enqueue(new Callback<ODataResponse<UserDto>>() {
            @Override
            public void onResponse(Call<ODataResponse<UserDto>> call, Response<ODataResponse<UserDto>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().value.isEmpty()) {
                    UserDto user = response.body().value.get(0);

                    txtUsername.setText("Username: " + user.Username);
                    txtEmail.setText("Email: " + user.Email);
                    txtRole.setText("Role: " + user.Role);
                    txtStatus.setText("Trạng thái: " + (user.Status ? "Đang hoạt động" : "Bị khoá"));

                    if ("admin".equalsIgnoreCase(user.Role)) {
                        btnDashboard.setVisibility(View.VISIBLE);
                        btnDashboard.setOnClickListener(v -> {
                            startActivity(new Intent(requireContext(), DashboardActivity.class));
                        });
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date now = new Date();

                    Date createdAt = new Date(user.CreatedAt);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(createdAt);
                    cal.add(Calendar.MONTH, 1);
                    Date firstMonthEnd = cal.getTime();

                    String createText = "Ngày tạo: " + sdf.format(createdAt);
                    if (now.before(firstMonthEnd)) {
                        long daysLeft = (firstMonthEnd.getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
                        createText += " (Còn ưu đãi thành viên mới " + daysLeft + " ngày)";
                    } else {
                        createText += " (Đã hết ưu đãi thành viên mới)";
                    }

                    txtCreatedAt.setText(createText);

                    if (user.PremiumExpiredAt > 0) {
                        Date premiumDate = new Date(user.PremiumExpiredAt);
                        if (premiumDate.after(now)) {
                            long daysLeft = (premiumDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24);
                            txtPremium.setText("Premium hết hạn: " + sdf.format(premiumDate) + " (" + daysLeft + " ngày còn lại)");
                        } else {
                            txtPremium.setText("Premium đã hết hạn: " + sdf.format(premiumDate) + " (Đã hết hạn)");
                        }
                    } else {
                        txtPremium.setText("Premium: Chưa đăng ký");
                    }
                } else {
                    startActivity(new Intent(requireContext(), LoginActivity.class));
                    Toast.makeText(requireContext(), "Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ODataResponse<UserDto>> call, Throwable t) {
                txtUsername.setText("Không tải được thông tin.");
            }
        });
    }


}