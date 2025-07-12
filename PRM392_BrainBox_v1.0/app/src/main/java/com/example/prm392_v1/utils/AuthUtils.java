    package com.example.prm392_v1.utils;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.example.prm392_v1.data.entity.User;
    import com.example.prm392_v1.ui.main.HomeActivity;
    import com.example.prm392_v1.ui.main.MainActivity;

    public class AuthUtils {
        private static final String PREF_NAME = "auth_prefs";
        private static final String KEY_TOKEN = "jwt_token";

        public static void saveToken(Context context, String token) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_TOKEN, token).apply();
        }

        public static String getToken(Context context) {
            SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            return prefs.getString(KEY_TOKEN, null);
        }

        public static void navigateToScreenByRole(Context context, String token) {
            User user = JwtUtils.parseUserFromToken(token);
            if (user == null) {
                Toast.makeText(context, "Không đọc được thông tin người dùng từ token", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent;
            switch (user.role.toLowerCase()) {
                case "admin":
                    intent = new Intent(context, HomeActivity.class);
                    break;
                case "teacher":
                    intent = new Intent(context, HomeActivity.class);
                    break;
                case "user":
                    intent = new Intent(context, HomeActivity.class);
                    break;
                default:
                    intent = new Intent(context, MainActivity.class);
                    break;
            }

            context.startActivity(intent);
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).finish();
            }
        }

        public static String getUserIdFromToken(Context context) {
            String token = getToken(context);
            if (token == null) {
                return null;
            }
            User user = JwtUtils.parseUserFromToken(token);
            if (user == null) {
                Toast.makeText(context, "Không đọc được thông tin người dùng từ token", Toast.LENGTH_SHORT).show();
                return null;
            }
            return String.valueOf(user.id); 
        }
    }