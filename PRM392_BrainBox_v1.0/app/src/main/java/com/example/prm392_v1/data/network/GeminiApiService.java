package com.example.prm392_v1.data.network;

import com.example.prm392_v1.data.model.gemini.GeminiRequest;
import com.example.prm392_v1.data.model.gemini.GeminiResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    Call<GeminiResponse> generateContent(
            @Body GeminiRequest body,
            @Query("key") String apiKey
    );
}