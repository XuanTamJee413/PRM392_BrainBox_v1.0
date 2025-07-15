package com.example.prm392_v1.data.network;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        SharedPreferences prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt_token", null);

        Request.Builder requestBuilder = chain.request().newBuilder();

        if (token != null && !token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + token);
            android.util.Log.d("AuthInterceptor", "Adding Authorization header with token: " + token);
        } else {
            android.util.Log.d("AuthInterceptor", "No token found in SharedPreferences");
        }

        return chain.proceed(requestBuilder.build());
    }
}