package com.example.prm392_v1;

import android.app.Application;
import android.content.SharedPreferences;

public class BrainBoxApp extends Application {
    private static String jwtToken;

    @Override
    public void onCreate() {
        super.onCreate();
        // Load token from SharedPreferences on app start
        SharedPreferences prefs = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        jwtToken = prefs.getString("jwt_token", null);
    }

    public static String getJwtToken() {
        return jwtToken;
    }

    public static void setJwtToken(String token) {
        jwtToken = token;
    }
}