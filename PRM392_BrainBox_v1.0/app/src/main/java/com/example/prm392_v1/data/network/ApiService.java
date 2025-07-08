package com.example.prm392_v1.data.network;
import com.example.prm392_v1.data.model.LoginRequest;
import com.example.prm392_v1.data.model.LoginResponse;
import com.example.prm392_v1.data.model.Quiz;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import com.example.prm392_v1.data.model.ODataResponse;
public interface ApiService {

    // Endpoint đăng nhập
    // Thay thế "api/auth/login" bằng đường dẫn đúng của bạn
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // Endpoint lấy danh sách quiz
    @GET("odata/quizzes")
    Call<ODataResponse<Quiz>> getAllQuizzes();
}
