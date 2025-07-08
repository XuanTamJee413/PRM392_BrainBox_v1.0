package com.example.prm392_v1.data.network;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.LoginRequest;
import com.example.prm392_v1.data.model.LoginResponse;
import com.example.prm392_v1.data.model.Quiz;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.QuizDto;
import com.example.prm392_v1.data.model.UserDto;

public interface ApiService {

    // Endpoint đăng nhập
    // Thay thế "api/auth/login" bằng đường dẫn đúng của bạn
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Endpoint lấy danh sách quiz
    @GET("odata/quizzes")
    Call<ODataResponse<Quiz>> getAllQuizzes();

    // tamnx get top 5 document & top latest quiz
    @GET("odata/Documents")
    Call<ODataResponse<DocumentDto>> getTopDocuments(
            @Query("$filter") String filter,
            @Query("$orderby") String orderBy,
            @Query("$top") int top,
            @Query("$expand") String expand
    );
    @GET("odata/Quizzes")
    Call<ODataResponse<QuizDto>> getLatestQuizzes(
            @Query("$filter") String filter,
            @Query("$orderby") String orderby,
            @Query("$top") Integer top,
            @Query("$expand") String expand
    );
    // ApiService.java
    @GET("odata/Users")
    Call<ODataResponse<UserDto>> getUserById(@Query("$filter") String filter);

    // end tamnx
}
