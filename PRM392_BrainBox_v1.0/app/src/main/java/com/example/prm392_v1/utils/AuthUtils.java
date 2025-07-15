package com.example.prm392_v1.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_v1.BrainBoxApp;
import com.example.prm392_v1.data.entity.User;
import com.example.prm392_v1.ui.auth.LoginActivity;
import com.example.prm392_v1.ui.main.HomeActivity;

public class AuthUtils {

    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";

    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
        BrainBoxApp.setJwtToken(token); // Keep for backward compatibility
    }

    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        if (token != null) {
            BrainBoxApp.setJwtToken(token); // Sync with BrainBoxApp
        }
        return token;
    }

    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
        BrainBoxApp.setJwtToken(null);
    }

    public static void navigateToScreenByRole(Context context, String token) {
        User user = JwtUtils.parseUserFromToken(token);
        if (user == null || token == null) {
            Toast.makeText(context, "Invalid session. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).finish();
            }
            return;
        }

        Intent intent;
        switch (user.role.toLowerCase()) {
            case "admin":
            case "teacher":
            case "user":
                intent = new Intent(context, HomeActivity.class);
                break;
            default:
                intent = new Intent(context, LoginActivity.class);
                break;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            Toast.makeText(context, "Could not read user information from token", Toast.LENGTH_SHORT).show();
            return null;
        }
        return String.valueOf(user.id);
    }
}