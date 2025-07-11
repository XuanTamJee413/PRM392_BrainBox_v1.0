package com.example.prm392_v1.data.network;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.LoginRequest;
import com.example.prm392_v1.data.model.LoginResponse;
import com.example.prm392_v1.data.model.Quiz;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import com.example.prm392_v1.data.model.QuizUpdateDto;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.QuizDto;
import com.example.prm392_v1.data.model.RatingQuiz;
import com.example.prm392_v1.data.model.RatingQuizRequest;
import com.example.prm392_v1.data.model.RegisterRequest;
import com.example.prm392_v1.data.model.UserDto;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.QuizCreateRequest;

public interface ApiService {


    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);


    @GET("odata/quizzes")
    Call<ODataResponse<Quiz>> getAllQuizzes();
    @GET("odata/quizzes")
    Call<ODataResponse<Quiz>> getQuizzesByFilter(@Query("$filter") String filter);
    @POST("odata/quizzes")
    Call<Quiz> createQuizWithFlashcards(@Body QuizCreateRequest request);
    @PUT("odata/quizzes({id})")
    Call<Void> updateQuiz(@Path("id") int quizId, @Body QuizUpdateDto request);
    @GET("odata/quizzes({id})")
    Call<Quiz> getQuizDetails(@Path("id") int quizId, @Query("$expand") String expand);
    @GET("odata/flashcards")
    Call<ODataResponse<Flashcard>> getFlashcardsByFilter(@Query("$filter") String filter);
    @GET("api/RatingQuizzes")
    Call<RatingQuiz> getMyRatingForQuiz(@Query("quizId") int quizId);

    @POST("api/RatingQuizzes")
    Call<Void> submitRating(@Body RatingQuizRequest request);
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
    @GET("odata/Users")
    Call<ODataResponse<UserDto>> getUserById(@Query("$filter") String filter);
    @PATCH("odata/Users({id})")
    Call<Void> updatePremium(@Path("id") int id, @Body Map<String, Object> updates);

    @POST("auth/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    // end tamnx
    @GET("odata/flashcards")
    Call<ODataResponse<Flashcard>> getFlashcardsForQuiz(@Query("$filter") String filter);
}
