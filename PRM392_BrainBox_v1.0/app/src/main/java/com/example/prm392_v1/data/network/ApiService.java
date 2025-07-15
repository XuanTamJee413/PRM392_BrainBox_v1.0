package com.example.prm392_v1.data.network;

import com.example.prm392_v1.data.model.DocumentDetail;
import com.example.prm392_v1.data.model.DocumentDto;
import com.example.prm392_v1.data.model.DocumentCreateDto;
import com.example.prm392_v1.data.model.LoginRequest;
import com.example.prm392_v1.data.model.LoginResponse;
import com.example.prm392_v1.data.model.Quiz;
import com.example.prm392_v1.data.model.Comment;
import com.example.prm392_v1.data.model.ODataResponse;
import com.example.prm392_v1.data.model.QuizUpdateDto;
import com.example.prm392_v1.data.model.QuizDto;
import com.example.prm392_v1.data.model.RatingQuiz;
import com.example.prm392_v1.data.model.RatingQuizRequest;
import com.example.prm392_v1.data.model.RegisterRequest;
import com.example.prm392_v1.data.model.UserDto;
import com.example.prm392_v1.data.model.Flashcard;
import com.example.prm392_v1.data.model.QuizCreateRequest;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @POST("api/auth/register")
    Call<LoginResponse> register(@Body RegisterRequest request);

    @GET("odata/flashcards")
    Call<ODataResponse<Flashcard>> getFlashcardsForQuiz(@Query("$filter") String filter);

    @GET("odata/Documents")
    Call<ODataResponse<DocumentDto>> getAllDocuments(
            @Query("$filter") String filter,
            @Query("$expand") String expand
    );

    @GET("odata/Documents({id})")
    Call<DocumentDto> getDocumentById(
            @Path("id") int docId,
            @Query("$expand") String expand
    );

    @POST("odata/Documents")
    Call<DocumentDto> createDocument(@Body DocumentDto document);

    @PUT("odata/Documents({id})")
    Call<Void> updateDocument(@Path("id") int docId, @Body DocumentDto document);
    @POST("odata/Documents")
    Call<DocumentDto> createDocumentNew(@Body DocumentCreateDto document);

    @PUT("odata/Documents({id})")
    Call<Void> updateDocumentNew(@Path("id") int docId, @Body DocumentCreateDto document);

    @PATCH("odata/Documents({id})")
    Call<Void> patchDocument(@Path("id") int docId, @Body DocumentDto document);

    @DELETE("odata/Documents({id})")
    Call<Void> deleteDocument(@Path("id") int docId);

    @GET("odata/DocumentDetails")
    Call<ODataResponse<DocumentDetail>> getDocumentDetails(
            @Query("$filter") String filter
    );

    @POST("odata/DocumentDetails")
    Call<DocumentDetail> createDocumentDetail(@Body DocumentDetail detail);

    @PUT("odata/DocumentDetails({id})")
    Call<Void> updateDocumentDetail(@Path("id") int detailId, @Body DocumentDetail detail);

    @DELETE("odata/DocumentDetails({id})")
    Call<Void> deleteDocumentDetail(@Path("id") int detailId);

    @GET("odata/Comments")
    Call<ODataResponse<Comment>> getCommentsByDocDetail(@Query("$filter") String filter);

    @GET("api/RatingQuizzes/getRatingQuizById")
    Call<List<RatingQuiz>> getRatingsForQuiz(@Query("quizId") int quizId);

    @POST("odata/Comments")
    Call<Comment> createComment(@Body Comment comment);
}