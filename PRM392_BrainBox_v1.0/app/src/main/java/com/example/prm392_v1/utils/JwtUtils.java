    package com.example.prm392_v1.utils;

    import android.util.Base64;

    import com.example.prm392_v1.data.entity.User;

    import org.json.JSONObject;

    public class JwtUtils {
        public static User parseUserFromToken(String token) {
            try {
                String[] parts = token.split("\\.");
                if (parts.length != 3) return null;

                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE));
                JSONObject json = new JSONObject(payload);

                // Check token expiration
                long exp = json.optLong("exp") * 1000; // Convert to milliseconds
                if (exp < System.currentTimeMillis()) {
                    return null; // Token expired
                }

                User user = new User();
                user.id = json.optInt("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier");
                user.username = json.optString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name");
                user.email = json.optString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
                user.role = json.optString("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");

                return user;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static String getUserIdFromToken(String token) {
            try {
                String[] parts = token.split("\\.");
                if (parts.length != 3) return null;

                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE), "UTF-8");
                JSONObject json = new JSONObject(payload);

                // Trả về trực tiếp chuỗi ID
                return json.optString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
